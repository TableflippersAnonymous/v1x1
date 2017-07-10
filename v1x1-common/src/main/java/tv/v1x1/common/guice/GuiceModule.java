package tv.v1x1.common.guice;

import brave.Tracer;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;
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
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import tv.v1x1.common.config.ConfigType;
import tv.v1x1.common.config.Permission;
import tv.v1x1.common.dao.DAOArtificialNeuralNetwork;
import tv.v1x1.common.dao.DAOChannelConfiguration;
import tv.v1x1.common.dao.DAOConfigurationDefinition;
import tv.v1x1.common.dao.DAOGlobalConfiguration;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dao.DAOJoinedTwitchChannel;
import tv.v1x1.common.dao.DAOKeyValueEntry;
import tv.v1x1.common.dao.DAOLanguage;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dao.DAOTenantConfiguration;
import tv.v1x1.common.dao.DAOTenantGroup;
import tv.v1x1.common.dao.DAOThirdPartyCredential;
import tv.v1x1.common.dao.DAOTwitchOauthToken;
import tv.v1x1.common.dto.core.ModuleInstance;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.modules.CassandraConfig;
import tv.v1x1.common.modules.ChannelConfiguration;
import tv.v1x1.common.modules.GlobalConfiguration;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.modules.ModuleSettings;
import tv.v1x1.common.modules.TenantConfiguration;
import tv.v1x1.common.modules.ZipkinConfig;
import tv.v1x1.common.services.cache.CacheManager;
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
import tv.v1x1.common.services.pubsub.TopicManager;
import tv.v1x1.common.services.pubsub.TopicManagerImpl;
import tv.v1x1.common.services.queue.MessageQueueManager;
import tv.v1x1.common.services.queue.MessageQueueManagerImpl;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.services.state.MembershipService;
import tv.v1x1.common.services.state.StateManager;
import tv.v1x1.common.services.stats.NoopStatsCollector;
import tv.v1x1.common.services.stats.StatsCollector;
import tv.v1x1.common.services.stats.ZipkinStatsCollector;
import tv.v1x1.common.services.twitch.TwitchApi;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Reporter;
import zipkin.reporter.Sender;
import zipkin.reporter.okhttp3.OkHttpSender;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

/**
 * Created by jcarter on 1/27/17.
 */
public class GuiceModule<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration, W extends ChannelConfiguration> extends AbstractModule {
    private final T settings;
    private final Module<T, U, V, W> module;

    public GuiceModule(final T settings, final Module<T, U, V, W> module) {
        this.settings = settings;
        this.module = module;
    }

    @Override
    protected void configure() {
        bind(DAOArtificialNeuralNetwork.class);
        bind(DAOChannelConfiguration.class);
        bind(DAOConfigurationDefinition.class);
        bind(DAOGlobalConfiguration.class);
        bind(DAOGlobalUser.class);
        bind(DAOJoinedTwitchChannel.class);
        bind(DAOKeyValueEntry.class);
        bind(DAOLanguage.class);
        bind(DAOTenant.class);
        bind(DAOTenantConfiguration.class);
        bind(DAOTenantGroup.class);
        bind(DAOThirdPartyCredential.class);
        bind(DAOTwitchOauthToken.class);

        bind(I18n.class);

        bind(MessageQueueManager.class).to(MessageQueueManagerImpl.class);
        bind(Deduplicator.class);
        bind(ModuleRegistry.class);
        bind(StatsCollector.class).to(ZipkinStatsCollector.class);
        bind(LockManager.class);
        bind(CacheManager.class);
        bind(DAOManager.class);
        bind(DisplayNameService.class);
        bind(MembershipService.class);
        bind(StateManager.class);
        bind(ConfigurationProvider.class);
        bind(TenantConfigurationProvider.class);
        bind(ChannelConfigurationProvider.class);
        bind(TopicManager.class).to(TopicManagerImpl.class);
    }

    @Provides
    public ModuleSettings getSettings() {
        return settings;
    }

    @Provides
    public Config getRedissonConfig(final ModuleSettings settings) {
        return settings.getRedissonConfig();
    }

    @Provides @Singleton
    public RedissonClient getRedisson(final Config config) {
        return Redisson.create(config);
    }

    @Provides
    public CassandraConfig getCassandraConfig(final ModuleSettings settings) {
        return settings.getCassandraConfig();
    }

    @Provides
    public ZipkinConfig getZipkinConfig(final ModuleSettings settings) {
        return settings.getZipkinConfig();
    }

    @Provides
    public Module getModule() {
        return module;
    }

    @Provides @Singleton
    public tv.v1x1.common.dto.core.Module getDto(final Module module) {
        return module.toDto();
    }

    @Provides @PersistentGlobal @Singleton
    public KeyValueStore getPersistentGlobalKVS(final DAOKeyValueEntry daoKeyValueEntry) {
        return new PersistentKeyValueStoreImpl(daoKeyValueEntry);
    }

    @Provides @PersistentModule @Singleton
    public KeyValueStore getPersistentModuleKVS(final DAOKeyValueEntry daoKeyValueEntry, final tv.v1x1.common.dto.core.Module module) {
        return new PersistentKeyValueStoreImpl(daoKeyValueEntry, module);
    }

    @Provides @TemporaryGlobal @Singleton
    public KeyValueStore getTemporaryGlobalKVS(final RedissonClient redissonClient) {
        return new TemporaryKeyValueStoreImpl(redissonClient);
    }

    @Provides @TemporaryModule @Singleton
    public KeyValueStore getTemporaryModuleKVS(final RedissonClient redissonClient, final tv.v1x1.common.dto.core.Module module) {
        return new TemporaryKeyValueStoreImpl(redissonClient, module);
    }

    @Provides @Singleton
    public CuratorFramework getCurator(final ModuleSettings settings) {
        final CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(settings.getZookeeperConnectionString(), new BoundedExponentialBackoffRetry(50, 1000, 29));
        curatorFramework.start();
        return curatorFramework;
    }

    @Provides @Singleton
    public ModuleInstance getModuleInstance(final Module module) {
        return module.toModuleInstance();
    }

    @Provides @Singleton
    public Cluster getCassandraCluster(final CassandraConfig cassandraConfig) {
        return Cluster.builder() /* Yay, options! */
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
    }

    @Provides @Singleton
    public Session getCassandraSession(final CassandraConfig cassandraConfig, final Cluster cassandraCluster) {
        final Session session = cassandraCluster.connect();
        session.execute(new SimpleStatement("USE " + cassandraConfig.getKeyspace()));
        return session;
    }

    @Provides @Singleton
    public MappingManager getMappingManager(final Session session) {
        return new MappingManager(session);
    }

    @Provides @Named("globalConfigurationClass") @Singleton
    public Class getGlobalConfigurationClass(final Module module) {
        return module.getGlobalConfigurationClass();
    }

    @Provides @Named("tenantConfigurationClass") @Singleton
    public Class getTenantConfigurationClass(final Module module) {
        return module.getTenantConfigurationClass();
    }

    @Provides @Named("channelConfigurationClass") @Singleton
    public Class getChannelConfigurationClass(final Module module) {
        return module.getChannelConfigurationClass();
    }

    @Provides @Singleton
    public TwitchApi getTwitchApi(final Module module) {
        return new TwitchApi(
                new String(module.requireCredential("Common|Twitch|ClientId")),
                new String(module.requireCredential("Common|Twitch|OAuthToken")),
                new String(module.requireCredential("Common|Twitch|ClientSecret")),
                new String(module.requireCredential("Common|Twitch|RedirectUri")));
    }

    @Provides @Singleton
    public Sender getSender(final ZipkinConfig zipkinConfig) {
        if(zipkinConfig.isEnabled())
            return OkHttpSender.create(zipkinConfig.getUri());
        else
            return null;
    }

    @Provides @Singleton
    public Reporter<Span> getReporter(@Nullable final Sender sender) {
        if(sender == null)
            return Reporter.NOOP;
        return AsyncReporter.builder(sender).build();
    }

    @Provides @Singleton
    public CurrentTraceContext getCurrentTraceContext() {
        return new CurrentTraceContext.Default();
    }

    @Provides @Singleton
    public Tracing getTracing(final Reporter<Span> reporter, final CurrentTraceContext currentTraceContext) {
        return Tracing.newBuilder()
                .localServiceName("v1x1-" + module.getName())
                .reporter(reporter)
                .currentTraceContext(currentTraceContext)
                .build();
    }

    @Provides @Singleton
    public Tracer getTracer(final Tracing tracing) {
        return tracing.tracer();
    }
}
