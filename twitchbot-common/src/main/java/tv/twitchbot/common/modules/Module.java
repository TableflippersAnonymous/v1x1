package tv.twitchbot.common.modules;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.*;
import com.datastax.driver.extras.codecs.enums.EnumOrdinalCodec;
import com.datastax.driver.mapping.MappingManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.dropwizard.util.Generics;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonNodeInitializer;
import org.redisson.client.codec.Codec;
import org.redisson.codec.CodecProvider;
import org.redisson.config.Config;
import org.redisson.config.ConfigSupport;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.connection.balancer.LoadBalancer;
import org.redisson.liveobject.provider.ResolverProvider;
import tv.twitchbot.common.dto.core.ModuleInstance;
import tv.twitchbot.common.dto.core.Tenant;
import tv.twitchbot.common.dto.db.Platform;
import tv.twitchbot.common.dto.messages.Message;
import tv.twitchbot.common.dto.messages.Request;
import tv.twitchbot.common.dto.messages.Response;
import tv.twitchbot.common.dto.messages.requests.ModuleShutdownRequest;
import tv.twitchbot.common.dto.messages.responses.ModuleShutdownResponse;
import tv.twitchbot.common.rpc.client.ServiceClient;
import tv.twitchbot.common.services.coordination.LoadBalancingDistributor;
import tv.twitchbot.common.services.coordination.LoadBalancingDistributorImpl;
import tv.twitchbot.common.services.coordination.ModuleRegistry;
import tv.twitchbot.common.services.persistence.*;
import tv.twitchbot.common.services.queue.MessageQueue;
import tv.twitchbot.common.services.queue.MessageQueueManager;
import tv.twitchbot.common.services.queue.MessageQueueManagerImpl;
import tv.twitchbot.common.services.stats.NoopStatsCollector;
import tv.twitchbot.common.services.stats.StatsCollector;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/4/16.
 */
public abstract class Module<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration> {
    /* Config */
    private T settings;
    private U globalConfig;
    private UUID instanceId = UUID.randomUUID();

    /* Queues */
    private MessageQueueManager messageQueueManager;

    /* Persistence */
    private KeyValueStore temporaryKeyValueStore;
    private KeyValueStore temporaryGlobalKeyValueStore;
    private KeyValueStore persistentKeyValueStore;
    private KeyValueStore persistentGlobalKeyValueStore;
    private Cluster cassandraCluster;
    private Session cassandraSession;
    private MappingManager mappingManager;
    private DAOManager daoManager;
    private Deduplicator deduplicator;

    /* Services */
    private ModuleRegistry moduleRegistry;
    private Map<Class<? extends ServiceClient>, ServiceClient> serviceClientMap = new ConcurrentHashMap<>();
    private Map<String, LoadBalancingDistributor> loadBalancingDistributorMap = new ConcurrentHashMap<>();
    private StatsCollector statsCollector;

    /* Third-Party Clients */
    private CuratorFramework curatorFramework;

    /* Designed to be overridden */
    public abstract String getName();
    protected abstract void handle(Message message);
    protected abstract void initialize();
    protected abstract void shutdown();

    /* ******************************* Initialization ******************************* */
    private void parseArgs(final String[] args) throws IOException {
        if(args.length != 1) {
            System.err.println("Error.  Expected 1 argument: config.yml");
            throw new RuntimeException("Error.  Expected 1 argument: config.yml");
        }
        loadConfig(args[0]);
    }

    private void loadConfig(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        /* Redisson support */
        mapper.addMixIn(MasterSlaveServersConfig.class, ConfigSupport.MasterSlaveServersConfigMixIn.class);
        mapper.addMixIn(SingleServerConfig.class, ConfigSupport.SingleSeverConfigMixIn.class);
        mapper.addMixIn(Config.class, ConfigSupport.ConfigMixIn.class);
        mapper.addMixIn(CodecProvider.class, ConfigSupport.ClassMixIn.class);
        mapper.addMixIn(ResolverProvider.class, ConfigSupport.ClassMixIn.class);
        mapper.addMixIn(Codec.class, ConfigSupport.ClassMixIn.class);
        mapper.addMixIn(RedissonNodeInitializer.class, ConfigSupport.ClassMixIn.class);
        mapper.addMixIn(LoadBalancer.class, ConfigSupport.ClassMixIn.class);

        settings = mapper.readValue(new File(filename), getSettingsClass());
    }

    private void initializeInternal() {
        Config redissonConfig = settings.getRedissonConfig();
        RedissonClient client = Redisson.create(redissonConfig);
        messageQueueManager = new MessageQueueManagerImpl(client);

        temporaryKeyValueStore = new TemporaryKeyValueStoreImpl(client, toDto());
        temporaryGlobalKeyValueStore = new TemporaryKeyValueStoreImpl(client);
        deduplicator = new Deduplicator(client, toDto());

        curatorFramework = CuratorFrameworkFactory.newClient(settings.getZookeeperConnectionString(), new BoundedExponentialBackoffRetry(50, 1000, 50));
        curatorFramework.start();
        moduleRegistry = new ModuleRegistry(curatorFramework, toModuleInstance());
        statsCollector = new NoopStatsCollector();

        CassandraConfig cassandraConfig = settings.getCassandraConfig();
        cassandraCluster = Cluster.builder() /* Yay, options! */
                .withClusterName(cassandraConfig.getClusterName())
                .addContactPoints(cassandraConfig.getContactPoints().toArray(new String[] {}))
                .withPort(cassandraConfig.getPort())
                .withAuthProvider(cassandraConfig.isAuthenticated() ? new PlainTextAuthProvider(cassandraConfig.getUsername(), cassandraConfig.getPassword()) : AuthProvider.NONE)
                .withLoadBalancingPolicy(new TokenAwarePolicy(
                    LatencyAwarePolicy.builder(
                            DCAwareRoundRobinPolicy.builder()
                                .build()
                    )
                    .withExclusionThreshold(cassandraConfig.getExclusionThreshold())
                    .withScale(cassandraConfig.getScaleMs(), TimeUnit.MILLISECONDS)
                    .withRetryPeriod(cassandraConfig.getRetryMs(), TimeUnit.MILLISECONDS)
                    .withUpdateRate(cassandraConfig.getUpdateMs(), TimeUnit.MILLISECONDS)
                    .withMininumMeasurements(cassandraConfig.getMinimumMeasurements())
                    .build()
                ))
                .withQueryOptions(new QueryOptions()
                        .setConsistencyLevel(ConsistencyLevel.valueOf(cassandraConfig.getConsistencyLevel()))
                        .setSerialConsistencyLevel(ConsistencyLevel.valueOf(cassandraConfig.getSerialConsistencyLevel()))
                        .setFetchSize(cassandraConfig.getFetchSize())
                )
                .withReconnectionPolicy(new ExponentialReconnectionPolicy(cassandraConfig.getReconnectBaseDelayMs(), cassandraConfig.getReconnectMaxDelayMs()))
                .withRetryPolicy(new LoggingRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE))
                .withSocketOptions(new SocketOptions()
                        .setConnectTimeoutMillis(cassandraConfig.getConnectTimeoutMs())
                        .setKeepAlive(cassandraConfig.isTcpKeepAlive())
                )
                .withSpeculativeExecutionPolicy(new PercentileSpeculativeExecutionPolicy(
                        PerHostPercentileTracker.builder(cassandraConfig.getHighestTrackableLatencyMs()).build(),
                        cassandraConfig.getSpeculativeRetryPercentile(),
                        cassandraConfig.getSpeculativeMaxRetries()
                ))
                .withCodecRegistry(new CodecRegistry()
                        .register(new EnumOrdinalCodec<>(Platform.class))
                )
                .build();
        cassandraSession = cassandraCluster.connect();
        mappingManager = new MappingManager(cassandraSession);
        daoManager = new DAOManager(mappingManager);

        persistentGlobalKeyValueStore = new PersistentKeyValueStoreImpl(daoManager.getDaoKeyValueEntry());
        persistentKeyValueStore = new PersistentKeyValueStoreImpl(daoManager.getDaoKeyValueEntry(), toDto());
    }

    /* ******************************* CALL THIS FROM main() ******************************* */
    protected void entryPoint(final String[] args) throws Exception {
        parseArgs(args);
        initializeInternal();
        initialize();
        try {
            run();
        } finally {
            shutdown();
        }
        cleanup();
    }

    /* ******************************* MAIN LOOP ******************************* */
    private void run() throws Exception {
        MessageQueueManager mqm = getMessageQueueManager();
        MessageQueue mq = mqm.forName(getQueueName());
        for(;;) {
            try {
                Message message = mq.get();
                if(deduplicator.seenAndAdd(message.getMessageId()))
                    continue;
                if(message instanceof ModuleShutdownRequest) {
                    ModuleShutdownRequest msr = (ModuleShutdownRequest) message;
                    reply(msr, new ModuleShutdownResponse(toDto(), msr.getMessageId()));
                    break;
                }
                handle(message);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    /* ******************************* TEAR-DOWN ******************************* */
    private void cleanup() {
        cassandraSession.close();
        cassandraCluster.close();
        for(Map.Entry<String, LoadBalancingDistributor> entry : loadBalancingDistributorMap.entrySet())
            try {
                entry.getValue().shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        for(Map.Entry<Class<? extends ServiceClient>, ServiceClient> entry : serviceClientMap.entrySet())
            entry.getValue().shutdown();
        try {
            moduleRegistry.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        curatorFramework.close();
    }

    /* ******************************* SIMPLE GETTERS ******************************* */
    public MessageQueueManager getMessageQueueManager() {
        return messageQueueManager;
    }

    protected T getSettings() {
        return settings;
    }

    protected U getGlobalConfiguration() {
        return globalConfig;
    }

    protected V getTenantConfiguration(Tenant tenant) {
        return null;
    }

    protected KeyValueStore getTemporaryKeyValueStore() {
        return temporaryKeyValueStore;
    }

    protected KeyValueStore getTemporaryGlobalKeyValueStore() {
        return temporaryGlobalKeyValueStore;
    }

    protected KeyValueStore getPersistentKeyValueStore() {
        return persistentKeyValueStore;
    }

    protected KeyValueStore getPersistentGlobalKeyValueStore() {
        return persistentGlobalKeyValueStore;
    }

    protected StatsCollector getStatsCollector() {
        return statsCollector;
    }

    protected ModuleRegistry getModuleRegistry() {
        return moduleRegistry;
    }

    protected UUID getInstanceId() {
        return instanceId;
    }

    protected Session getCassandraSession() {
        return cassandraSession;
    }

    protected MappingManager getMappingManager() {
        return mappingManager;
    }

    protected DAOManager getDaoManager() {
        return daoManager;
    }

    protected Deduplicator getDeduplicator() {
        return deduplicator;
    }

    /* ******************************* COMPLEX GETTERS ******************************* */
    protected KeyValueStore getTemporaryTenantKeyValueStore(Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, temporaryKeyValueStore);
    }

    protected KeyValueStore getTemporaryGlobalTenantKeyValueStore(Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, temporaryGlobalKeyValueStore);
    }

    protected KeyValueStore getPersistentTenantKeyValueStore(Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, persistentKeyValueStore);
    }

    protected KeyValueStore getPersistentGlobalTenantKeyValueStore(Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, persistentGlobalKeyValueStore);
    }

    @SuppressWarnings("unchecked")
    protected <W extends ServiceClient<? extends Request, ? extends Response<? extends Request>>> W getServiceClient(Class<W> serviceClass) {
        if(!serviceClientMap.containsKey(serviceClass)) {
            try {
                serviceClientMap.put(serviceClass, serviceClass.getConstructor(Module.class).newInstance(this));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return (W) serviceClientMap.get(serviceClass);
    }

    protected LoadBalancingDistributor getLoadBalancingDistributor(String path, int redundancy) {
        if(!loadBalancingDistributorMap.containsKey(path)) {
            LoadBalancingDistributor loadBalancingDistributor = new LoadBalancingDistributorImpl(curatorFramework, path, redundancy);
            try {
                loadBalancingDistributor.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            loadBalancingDistributorMap.put(path, loadBalancingDistributor);
        }
        return loadBalancingDistributorMap.get(path);
    }

    /* ******************************* UTILITY METHODS ******************************* */
    public tv.twitchbot.common.dto.core.Module toDto() {
        return new tv.twitchbot.common.dto.core.Module(getName());
    }

    public ModuleInstance toModuleInstance() {
        return new ModuleInstance(new tv.twitchbot.common.dto.core.UUID(instanceId), toDto());
    }

    public void send(String queueName, Message message) {
        getMessageQueueManager().forName(queueName).add(message);
    }

    public void reply(Request request, Response response) {
        send(request.getResponseQueueName(), response);
    }

    protected String getQueueName() {
        return getMainQueueForModule(toDto());
    }

    protected String getMainQueueForModule(tv.twitchbot.common.dto.core.Module module) {
        return "Module|" + module.getName();
    }

    /* ******************************* PRIVATE METHODS ******************************* */
    private Class<T> getSettingsClass() {
        return Generics.getTypeParameter(getClass(), ModuleSettings.class);
    }

    private Class<U> getGlobalConfigurationClass() {
        return Generics.getTypeParameter(getClass(), GlobalConfiguration.class);
    }

    private Class<V> getTenantConfigurationClass() {
        return Generics.getTypeParameter(getClass(), TenantConfiguration.class);
    }
}
