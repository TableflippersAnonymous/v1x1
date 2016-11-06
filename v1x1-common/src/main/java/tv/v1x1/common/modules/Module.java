package tv.v1x1.common.modules;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PerHostPercentileTracker;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.datastax.driver.core.policies.ExponentialReconnectionPolicy;
import com.datastax.driver.core.policies.LatencyAwarePolicy;
import com.datastax.driver.core.policies.LoggingRetryPolicy;
import com.datastax.driver.core.policies.PercentileSpeculativeExecutionPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.config.ConfigScanner;
import tv.v1x1.common.config.ConfigType;
import tv.v1x1.common.config.Permission;
import tv.v1x1.common.dao.DAOConfigurationDefinition;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.GlobalConfigurationDefinition;
import tv.v1x1.common.dto.core.ModuleInstance;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.TenantConfigurationDefinition;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.messages.Message;
import tv.v1x1.common.dto.messages.Request;
import tv.v1x1.common.dto.messages.Response;
import tv.v1x1.common.dto.messages.requests.ModuleShutdownRequest;
import tv.v1x1.common.dto.messages.responses.ModuleShutdownResponse;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.rpc.client.ServiceClient;
import tv.v1x1.common.services.coordination.LoadBalancingDistributor;
import tv.v1x1.common.services.coordination.LoadBalancingDistributorImpl;
import tv.v1x1.common.services.coordination.ModuleRegistry;
import tv.v1x1.common.services.persistence.ChannelConfigurationProvider;
import tv.v1x1.common.services.persistence.ConfigurationProvider;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.persistence.Deduplicator;
import tv.v1x1.common.services.persistence.KeyValueStore;
import tv.v1x1.common.services.persistence.PersistentKeyValueStoreImpl;
import tv.v1x1.common.services.persistence.TemporaryKeyValueStoreImpl;
import tv.v1x1.common.services.persistence.TenantConfigurationProvider;
import tv.v1x1.common.services.persistence.TenantKeyValueStoreImpl;
import tv.v1x1.common.services.queue.MessageQueue;
import tv.v1x1.common.services.queue.MessageQueueManager;
import tv.v1x1.common.services.queue.MessageQueueManagerImpl;
import tv.v1x1.common.services.state.StateManager;
import tv.v1x1.common.services.stats.NoopStatsCollector;
import tv.v1x1.common.services.stats.StatsCollector;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 10/4/16.
 */
public abstract class Module<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration, W extends ChannelConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /* Config */
    private T settings;
    private ConfigurationProvider<U> globalConfigProvider;
    private TenantConfigurationProvider<V> tenantConfigProvider;
    private ChannelConfigurationProvider<W> channelConfigProvider;
    private final UUID instanceId = UUID.randomUUID();
    private String configFile;

    /* Queues */
    private MessageQueueManager messageQueueManager;

    /* Persistence */
    private RedissonClient redisson;
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
    private final Map<Class<? extends ServiceClient>, ServiceClient> serviceClientMap = new ConcurrentHashMap<>();
    private final Map<String, LoadBalancingDistributor> loadBalancingDistributorMap = new ConcurrentHashMap<>();
    private StatsCollector statsCollector;
    private I18n i18n;
    private StateManager stateManager;

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
            LOG.error("Expected 1 argument: config.yml");
            throw new RuntimeException("Error.  Expected 1 argument: config.yml");
        }
        loadConfig(args[0]);
    }

    private void loadConfig(final String filename) throws IOException {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        /* Redisson support */
        mapper.addMixIn(MasterSlaveServersConfig.class, ConfigSupport.MasterSlaveServersConfigMixIn.class);
        mapper.addMixIn(SingleServerConfig.class, ConfigSupport.SingleSeverConfigMixIn.class);
        mapper.addMixIn(Config.class, ConfigSupport.ConfigMixIn.class);
        mapper.addMixIn(CodecProvider.class, ConfigSupport.ClassMixIn.class);
        mapper.addMixIn(ResolverProvider.class, ConfigSupport.ClassMixIn.class);
        mapper.addMixIn(Codec.class, ConfigSupport.ClassMixIn.class);
        mapper.addMixIn(RedissonNodeInitializer.class, ConfigSupport.ClassMixIn.class);
        mapper.addMixIn(LoadBalancer.class, ConfigSupport.ClassMixIn.class);

        configFile = filename;
        settings = mapper.readValue(new File(filename), getSettingsClass());
    }

    private void initializeInternal() {
        final Config redissonConfig = settings.getRedissonConfig();
        redisson = Redisson.create(redissonConfig);
        messageQueueManager = new MessageQueueManagerImpl(redisson);

        temporaryKeyValueStore = new TemporaryKeyValueStoreImpl(redisson, toDto());
        temporaryGlobalKeyValueStore = new TemporaryKeyValueStoreImpl(redisson);
        deduplicator = new Deduplicator(redisson, toDto());

        curatorFramework = CuratorFrameworkFactory.newClient(settings.getZookeeperConnectionString(), new BoundedExponentialBackoffRetry(50, 1000, 50));
        curatorFramework.start();
        moduleRegistry = new ModuleRegistry(curatorFramework, toModuleInstance());
        statsCollector = new NoopStatsCollector();

        final CassandraConfig cassandraConfig = settings.getCassandraConfig();
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
                        .register(new EnumOrdinalCodec<>(Permission.class))
                        .register(new EnumOrdinalCodec<>(ConfigType.class))
                )
                .build();
        cassandraSession = cassandraCluster.connect();
        cassandraSession.execute(new SimpleStatement("USE " + cassandraConfig.getKeyspace()));
        mappingManager = new MappingManager(cassandraSession);
        daoManager = new DAOManager(mappingManager);

        persistentGlobalKeyValueStore = new PersistentKeyValueStoreImpl(daoManager.getDaoKeyValueEntry());
        persistentKeyValueStore = new PersistentKeyValueStoreImpl(daoManager.getDaoKeyValueEntry(), toDto());

        globalConfigProvider = new ConfigurationProvider<U>(toDto(), daoManager, getGlobalConfigurationClass());
        tenantConfigProvider = new TenantConfigurationProvider<V>(toDto(), daoManager, getTenantConfigurationClass());
        channelConfigProvider = new ChannelConfigurationProvider<W>(toDto(), daoManager, getChannelConfigurationClass());
        i18n = new I18n(daoManager);
        stateManager = new StateManager();

        updateConfigurationDefinitions();
    }

    /* ******************************* CALL THIS FROM main() ******************************* */
    protected void entryPoint(final String[] args) throws Exception {
        parseArgs(args);
        if(settings.getWaitStartMs() > 0) {
            LOG.info("Waiting {}ms before starting up...", settings.getWaitStartMs());
            Thread.sleep(settings.getWaitStartMs());
        }
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
        final MessageQueueManager mqm = getMessageQueueManager();
        final MessageQueue mq = mqm.forName(getQueueName());
        for(;;) {
            try {
                final Message message = mq.get();
                if(deduplicator.seenAndAdd(message.getMessageId()))
                    continue;
                if(message instanceof ModuleShutdownRequest) {
                    final ModuleShutdownRequest msr = (ModuleShutdownRequest) message;
                    reply(msr, new ModuleShutdownResponse(toDto(), msr.getMessageId()));
                    break;
                }
                handle(message);
            } catch (final Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    /* ******************************* TEAR-DOWN ******************************* */
    private void cleanup() {
        cassandraSession.close();
        cassandraCluster.close();
        for(final Map.Entry<String, LoadBalancingDistributor> entry : loadBalancingDistributorMap.entrySet())
            try {
                entry.getValue().shutdown();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        for(final Map.Entry<Class<? extends ServiceClient>, ServiceClient> entry : serviceClientMap.entrySet())
            entry.getValue().shutdown();
        try {
            moduleRegistry.shutdown();
        } catch (final IOException e) {
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

    public ConfigurationProvider<U> getGlobalConfigProvider() {
        return globalConfigProvider;
    }

    public TenantConfigurationProvider<V> getTenantConfigProvider() {
        return tenantConfigProvider;
    }

    public ChannelConfigurationProvider<W> getChannelConfigProvider() {
        return channelConfigProvider;
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

    public MappingManager getMappingManager() {
        return mappingManager;
    }

    public DAOManager getDaoManager() {
        return daoManager;
    }

    protected Deduplicator getDeduplicator() {
        return deduplicator;
    }

    public CuratorFramework getCuratorFramework() {
        return curatorFramework;
    }

    protected I18n getI18n() {
        return i18n;
    }

    public RedissonClient getRedisson() {
        return redisson;
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    protected String getConfigFile() {
        return configFile;
    }

    /* ******************************* COMPLEX GETTERS ******************************* */
    protected KeyValueStore getTemporaryTenantKeyValueStore(final Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, temporaryKeyValueStore);
    }

    protected KeyValueStore getTemporaryGlobalTenantKeyValueStore(final Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, temporaryGlobalKeyValueStore);
    }

    protected KeyValueStore getPersistentTenantKeyValueStore(final Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, persistentKeyValueStore);
    }

    protected KeyValueStore getPersistentGlobalTenantKeyValueStore(final Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, persistentGlobalKeyValueStore);
    }

    @SuppressWarnings("unchecked")
    public <W extends ServiceClient<? extends Request, ? extends Response<? extends Request>>> W getServiceClient(final Class<W> serviceClass) {
        if(!serviceClientMap.containsKey(serviceClass)) {
            try {
                serviceClientMap.put(serviceClass, serviceClass.getConstructor(Module.class).newInstance(this));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return (W) serviceClientMap.get(serviceClass);
    }

    protected LoadBalancingDistributor getLoadBalancingDistributor(final String path, final int redundancy) {
        if(!loadBalancingDistributorMap.containsKey(path)) {
            final LoadBalancingDistributor loadBalancingDistributor = new LoadBalancingDistributorImpl(curatorFramework, path, redundancy);
            try {
                loadBalancingDistributor.start();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
            loadBalancingDistributorMap.put(path, loadBalancingDistributor);
        }
        return loadBalancingDistributorMap.get(path);
    }

    public U getGlobalConfiguration() {
        return getGlobalConfigProvider().getConfiguration();
    }

    public V getTenantConfiguration(final Tenant tenant) {
        return getTenantConfigProvider().getTenantConfiguration(tenant);
    }

    public W getChannelConfiguration(final Channel channel) {
        return getChannelConfigProvider().getChannelConfiguration(channel);
    }

    /* ******************************* UTILITY METHODS ******************************* */
    public tv.v1x1.common.dto.core.Module toDto() {
        return new tv.v1x1.common.dto.core.Module(getName());
    }

    public ModuleInstance toModuleInstance() {
        return new ModuleInstance(new tv.v1x1.common.dto.core.UUID(instanceId), toDto());
    }

    public void send(final String queueName, final Message message) {
        getMessageQueueManager().forName(queueName).add(message);
    }

    public void reply(final Request request, final Response response) {
        send(request.getResponseQueueName(), response);
    }

    protected String getQueueName() {
        return getMainQueueForModule(toDto());
    }

    public static String getMainQueueForModule(final tv.v1x1.common.dto.core.Module module) {
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

    private Class<W> getChannelConfigurationClass() {
        return Generics.getTypeParameter(getClass(), ChannelConfiguration.class);
    }

    private void updateConfigurationDefinitions() {
        final DAOConfigurationDefinition daoConfigurationDefinition = daoManager.getDaoConfigurationDefinition();
        final GlobalConfigurationDefinition globalConfigurationDefinition = ConfigScanner.scanGlobal(getGlobalConfigurationClass());
        if(globalConfigurationDefinition != null)
            daoConfigurationDefinition.put(globalConfigurationDefinition.toDB());
        final TenantConfigurationDefinition tenantConfigurationDefinition = ConfigScanner.scanTenant(getTenantConfigurationClass());
        if(tenantConfigurationDefinition != null)
            daoConfigurationDefinition.put(tenantConfigurationDefinition.toDB());
    }
}
