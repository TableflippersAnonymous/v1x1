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
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
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
import org.redisson.misc.URLBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import tv.v1x1.common.config.ConfigScanner;
import tv.v1x1.common.config.ConfigType;
import tv.v1x1.common.config.Permission;
import tv.v1x1.common.dao.DAOConfigurationDefinition;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChannelConfigurationDefinition;
import tv.v1x1.common.dto.core.GlobalConfigurationDefinition;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.dto.core.ModuleInstance;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.TenantConfigurationDefinition;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.ThirdPartyCredential;
import tv.v1x1.common.dto.db.TwitchOauthToken;
import tv.v1x1.common.dto.messages.Message;
import tv.v1x1.common.dto.messages.Request;
import tv.v1x1.common.dto.messages.Response;
import tv.v1x1.common.dto.messages.requests.ModuleShutdownRequest;
import tv.v1x1.common.dto.messages.responses.ModuleShutdownResponse;
import tv.v1x1.common.guice.GuiceModule;
import tv.v1x1.common.guice.PersistentGlobal;
import tv.v1x1.common.guice.PersistentModule;
import tv.v1x1.common.guice.TemporaryGlobal;
import tv.v1x1.common.guice.TemporaryModule;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.rpc.client.ServiceClient;
import tv.v1x1.common.services.cache.CacheManager;
import tv.v1x1.common.services.coordination.LoadBalancingDistributor;
import tv.v1x1.common.services.coordination.LoadBalancingDistributorImpl;
import tv.v1x1.common.services.coordination.LockManager;
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
import tv.v1x1.common.services.twitch.TwitchApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/4/16.
 */
public abstract class Module<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration, W extends ChannelConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /* Config */
    private T settings;
    private final UUID instanceId = UUID.randomUUID();
    private String configFile;

    /* Services */
    private final Map<Class<? extends ServiceClient>, ServiceClient> serviceClientMap = new ConcurrentHashMap<>();
    private final Map<String, LoadBalancingDistributor> loadBalancingDistributorMap = new ConcurrentHashMap<>();
    private TwitchApi twitchApi;
    private Injector injector;

    /* Designed to be overridden */
    public abstract String getName();
    protected abstract void handle(Message message);
    protected abstract void initialize();
    protected void preinit() {
        /* No implementation */
    }
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
        final FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("classFilter", SimpleBeanPropertyFilter.filterOutAllExcept());
        mapper.setFilterProvider(filterProvider);
        URLBuilder.init();

        configFile = filename;
        final String settingsString = new String(Files.readAllBytes(Paths.get(filename)));
        final String fixedSettings = settingsString.replace("{{module_name}}", getClass().getCanonicalName());
        settings = mapper.readValue(fixedSettings, getSettingsClass());
    }

    private void initializeInternal() {
        injector = Guice.createInjector(new GuiceModule<>(settings, this));

        getModuleRegistry();

        registerGlobalMessages();

        twitchApi = new TwitchApi(new String(requireCredential("Common|Twitch|ClientId")), new String(requireCredential("Common|Twitch|OAuthToken")), new String(requireCredential("Common|Twitch|ClientSecret")), new String(requireCredential("Common|Twitch|RedirectUri")));

        updateConfigurationDefinitions();
    }

    /* ******************************* CALL THIS FROM main() ******************************* */
    protected void entryPoint(final String[] args) throws Exception {
        try {
            parseArgs(args);
            if (settings.getWaitStartMs() > 0) {
                LOG.info("Waiting {}ms before starting up...", settings.getWaitStartMs());
                Thread.sleep(settings.getWaitStartMs());
            }
            preinit();
            initializeInternal();
            initialize();
            try {
                run();
            } finally {
                shutdown();
            }
        } catch(final Exception e) {
            LOG.error("Uncaught exception during main loop:", e);
        } finally {
            try {
                cleanup();
            } finally {
                System.exit(0);
            }
        }
    }

    /* ******************************* MAIN LOOP ******************************* */
    private void run() throws Exception {
        final MessageQueueManager mqm = getMessageQueueManager();
        final MessageQueue mq = mqm.forName(getQueueName());
        for(;;) {
            try {
                final Message message = mq.get();
                if(getDeduplicator().seenAndAdd(message.getMessageId()))
                    continue;
                if(message instanceof ModuleShutdownRequest) {
                    final ModuleShutdownRequest msr = (ModuleShutdownRequest) message;
                    reply(msr, new ModuleShutdownResponse(toDto(), msr.getMessageId()));
                    break;
                }

                try (AutoCloseable messageId = MDC.putCloseable("messageId", message.getMessageId().getValue().toString())) {
                    handle(message);
                }
            } catch (final Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    /* ******************************* TEAR-DOWN ******************************* */
    private void cleanup() {
        getCassandraSession().close();
        getCassandraCluster().close();
        for(final Map.Entry<String, LoadBalancingDistributor> entry : loadBalancingDistributorMap.entrySet())
            try {
                entry.getValue().shutdown();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        for(final Map.Entry<Class<? extends ServiceClient>, ServiceClient> entry : serviceClientMap.entrySet())
            entry.getValue().shutdown();
        try {
            getModuleRegistry().shutdown();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        getCuratorFramework().close();
    }

    /* ******************************* SIMPLE GETTERS ******************************* */
    public MessageQueueManager getMessageQueueManager() {
        return injector.getInstance(MessageQueueManager.class);
    }

    protected T getSettings() {
        return settings;
    }

    public ConfigurationProvider<U> getGlobalConfigProvider() {
        return injector.getInstance(ConfigurationProvider.class);
    }

    public TenantConfigurationProvider<V> getTenantConfigProvider() {
        return injector.getInstance(TenantConfigurationProvider.class);
    }

    public ChannelConfigurationProvider<W> getChannelConfigProvider() {
        return injector.getInstance(ChannelConfigurationProvider.class);
    }

    protected KeyValueStore getTemporaryKeyValueStore() {
        return injector.getInstance(Key.get(KeyValueStore.class, TemporaryModule.class));
    }

    public KeyValueStore getTemporaryGlobalKeyValueStore() {
        return injector.getInstance(Key.get(KeyValueStore.class, TemporaryGlobal.class));
    }

    protected KeyValueStore getPersistentKeyValueStore() {
        return injector.getInstance(Key.get(KeyValueStore.class, PersistentModule.class));
    }

    protected KeyValueStore getPersistentGlobalKeyValueStore() {
        return injector.getInstance(Key.get(KeyValueStore.class, PersistentGlobal.class));
    }

    protected StatsCollector getStatsCollector() {
        return injector.getInstance(StatsCollector.class);
    }

    public ModuleRegistry getModuleRegistry() {
        return injector.getInstance(ModuleRegistry.class);
    }

    protected UUID getInstanceId() {
        return instanceId;
    }

    protected Session getCassandraSession() {
        return injector.getInstance(Session.class);
    }

    protected Cluster getCassandraCluster() {
        return injector.getInstance(Cluster.class);
    }

    public MappingManager getMappingManager() {
        return injector.getInstance(MappingManager.class);
    }

    public DAOManager getDaoManager() {
        return injector.getInstance(DAOManager.class);
    }

    protected Deduplicator getDeduplicator() {
        return injector.getInstance(Deduplicator.class);
    }

    public CuratorFramework getCuratorFramework() {
        return injector.getInstance(CuratorFramework.class);
    }

    protected I18n getI18n() {
        return injector.getInstance(I18n.class);
    }

    public RedissonClient getRedisson() {
        return injector.getInstance(RedissonClient.class);
    }

    public StateManager getStateManager() {
        return injector.getInstance(StateManager.class);
    }

    protected String getConfigFile() {
        return configFile;
    }

    public TwitchApi getTwitchApi() {
        return twitchApi;
    }

    public LockManager getLockManager() {
        return injector.getInstance(LockManager.class);
    }

    public CacheManager getCacheManager() {
        return injector.getInstance(CacheManager.class);
    }

    /* ******************************* COMPLEX GETTERS ******************************* */
    protected KeyValueStore getTemporaryTenantKeyValueStore(final Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, getTemporaryKeyValueStore());
    }

    protected KeyValueStore getTemporaryGlobalTenantKeyValueStore(final Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, getTemporaryGlobalKeyValueStore());
    }

    protected KeyValueStore getPersistentTenantKeyValueStore(final Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, getPersistentKeyValueStore());
    }

    protected KeyValueStore getPersistentGlobalTenantKeyValueStore(final Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, getPersistentGlobalKeyValueStore());
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
            final LoadBalancingDistributor loadBalancingDistributor = new LoadBalancingDistributorImpl(getCuratorFramework(), path, redundancy);
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

    public TwitchApi getTwitchApi(final String userId) {
        return new TwitchApi(new String(requireCredential("Common|Twitch|ClientId")), getTwitchOAuthToken(userId), new String(requireCredential("Common|Twitch|ClientSecret")), new String(requireCredential("Common|Twitch|RedirectUri")));
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

    protected byte[] getCredential(final String key) {
        final ThirdPartyCredential thirdPartyCredential = getDaoManager().getDaoThirdPartyCredential().get(key);
        if(thirdPartyCredential == null)
            return null;
        return thirdPartyCredential.credentialAsByteArray();
    }

    protected byte[] requireCredential(final String key) {
        final byte[] ret = getCredential(key);
        if(ret == null)
            throw new IllegalStateException("No such key: " + key);
        return ret;
    }

    protected String requireTwitchOAuthToken(final String userId) {
        final String oauthToken = getTwitchOAuthToken(userId);
        if(oauthToken == null)
            throw new IllegalStateException("No OAuth Token for: " + userId);
        return oauthToken;
    }

    protected String getTwitchOAuthToken(final String userId) {
        return getTwitchOAuthToken(getDaoManager().getDaoGlobalUser().getByUser(Platform.TWITCH, userId).toCore(), userId);
    }

    protected String requireTwitchOAuthToken(final GlobalUser globalUser, final String userId) {
        final String oauthToken = getTwitchOAuthToken(globalUser, userId);
        if(oauthToken == null)
            throw new IllegalStateException("No OAuth Token for: " + userId);
        return oauthToken;
    }

    protected String getTwitchOAuthToken(final GlobalUser globalUser, final String userId) {
        if(globalUser == null)
            return null;
        final TwitchOauthToken twitchOauthToken = getDaoManager().getDaoTwitchOauthToken().get(globalUser.getId().getValue(), userId);
        if(twitchOauthToken == null)
            return null;
        return twitchOauthToken.getOauthToken();
    }

    /* ******************************* PRIVATE METHODS ******************************* */
    private Class<T> getSettingsClass() {
        return Generics.getTypeParameter(getClass(), ModuleSettings.class);
    }

    public Class<U> getGlobalConfigurationClass() {
        return Generics.getTypeParameter(getClass(), GlobalConfiguration.class);
    }

    public Class<V> getTenantConfigurationClass() {
        return Generics.getTypeParameter(getClass(), TenantConfiguration.class);
    }

    public Class<W> getChannelConfigurationClass() {
        return Generics.getTypeParameter(getClass(), ChannelConfiguration.class);
    }

    private void updateConfigurationDefinitions() {
        final DAOConfigurationDefinition daoConfigurationDefinition = getDaoManager().getDaoConfigurationDefinition();
        final GlobalConfigurationDefinition globalConfigurationDefinition = ConfigScanner.scanGlobal(getGlobalConfigurationClass());
        if(globalConfigurationDefinition != null)
            daoConfigurationDefinition.put(globalConfigurationDefinition.toDB());
        final TenantConfigurationDefinition tenantConfigurationDefinition = ConfigScanner.scanTenant(getTenantConfigurationClass());
        if(tenantConfigurationDefinition != null)
            daoConfigurationDefinition.put(tenantConfigurationDefinition.toDB());
        final ChannelConfigurationDefinition channelConfigurationDefinition = ConfigScanner.scanChannel(getChannelConfigurationClass());
        if(channelConfigurationDefinition != null)
            daoConfigurationDefinition.put(channelConfigurationDefinition.toDB());
    }

    /* ******************************* LANGUAGE ******************************* */
    private void registerGlobalMessages() {
        tv.v1x1.common.dto.core.Module module = new tv.v1x1.common.dto.core.Module("_GLOBAL_");
        I18n.registerDefault(module, "generic.error",
                "Sorry %commander%, I've run into an internal problem... BibleThump My Bot Operators have been " +
                        "alerted. If you need help, please contact them with the time, date, your timezone, what happene" +
                        "d leading up to this, and this message: %message%. My apologies for the inconvenience!");
        I18n.registerDefault(module, "generic.noperms",
                "%commander%, sorry, looks like you don't have permission to do that.");
        I18n.registerDefault(module, "generic.twitchapi.error",
                "Sorry, there was a problem talking with the Twitch API. BibleThump If this persists, please con" +
                        "tact the Bot Operators");
        I18n.registerDefault(module, "generic.invalid.user",
                "%commander%, \"%input%\" is not a valid username here");
    }
}
