package tv.v1x1.common.modules;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.ObjectArrays;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import io.dropwizard.util.Generics;
import io.netty.util.internal.ConcurrentSet;
import io.sentry.Sentry;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonNodeInitializer;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.redisson.config.ConfigSupport;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.connection.balancer.LoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import tv.v1x1.common.dao.DAOConfigurationDefinition;
import tv.v1x1.common.dao.DAOThirdPartyCredential;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChannelGroup;
import tv.v1x1.common.dto.core.GlobalConfigurationDefinition;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.dto.core.ModuleInstance;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UserConfigurationDefinition;
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
import tv.v1x1.common.rpc.services.Service;
import tv.v1x1.common.scanners.config.ConfigScanner;
import tv.v1x1.common.scanners.i18n.I18nScanner;
import tv.v1x1.common.scanners.permission.PermissionScanner;
import tv.v1x1.common.services.coordination.LoadBalancingDistributor;
import tv.v1x1.common.services.coordination.LoadBalancingDistributorImpl;
import tv.v1x1.common.services.coordination.ModuleRegistry;
import tv.v1x1.common.services.persistence.ConfigurationProvider;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.persistence.Deduplicator;
import tv.v1x1.common.services.persistence.KeyValueStore;
import tv.v1x1.common.services.persistence.TenantKeyValueStoreImpl;
import tv.v1x1.common.services.persistence.UserConfigurationProvider;
import tv.v1x1.common.services.queue.MessageQueue;
import tv.v1x1.common.services.queue.MessageQueueManager;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.services.twitch.TwitchApi;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cobi on 10/4/16.
 */
public abstract class Module<T extends GlobalConfiguration, U extends UserConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /* Config */
    private T settings;
    private final UUID instanceId = UUID.randomUUID();
    private String configFile;

    /* Services */
    private final Map<Class<? extends ServiceClient>, ServiceClient> serviceClientMap = new ConcurrentHashMap<>();
    private final Map<String, LoadBalancingDistributor> loadBalancingDistributorMap = new ConcurrentHashMap<>();
    private Injector injector;
    private final Set<String> serviceQueues = new ConcurrentSet<>();
    private final Set<Service<?, ?>> services = new ConcurrentSet<>();
    private final Map<tv.v1x1.common.dto.core.UUID, ServiceClient<?, ?>> outstandingServiceRequests = new ConcurrentHashMap<>();

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
        mapper.addMixIn(Codec.class, ConfigSupport.ClassMixIn.class);
        mapper.addMixIn(RedissonNodeInitializer.class, ConfigSupport.ClassMixIn.class);
        mapper.addMixIn(LoadBalancer.class, ConfigSupport.ClassMixIn.class);
        final FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("classFilter", SimpleBeanPropertyFilter.filterOutAllExcept());
        mapper.setFilterProvider(filterProvider);

        configFile = filename;
        final String settingsString = new String(Files.readAllBytes(Paths.get(filename)));
        String fixedSettings = settingsString.replace("{{module_name}}", getClass().getCanonicalName());
        for(final Map.Entry<String, String> entry : System.getenv().entrySet())
            fixedSettings = fixedSettings.replace("{{ENV:" + entry.getKey() + "}}", entry.getValue());
        settings = mapper.readValue(fixedSettings, getGlobalConfigurationClass());
    }

    private void initializeInternal() {
        if(System.getenv("SENTRY_URL") != null)
            Sentry.init(System.getenv("SENTRY_URL"));
        getModuleRegistry();
        registerGlobalMessages();
        registerModuleMessages();
        registerModulePermissions();
        updateConfigurationDefinitions();
    }

    /* ******************************* CALL THIS FROM main() ******************************* */
    protected void entryPoint(final String[] args) {
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
                final Message message = mq.getWithOthers(() -> ObjectArrays.concat(getInstanceQueueName(), getServiceQueueNames()));
                if(getDeduplicator().seenAndAdd(message.getMessageId()))
                    continue;
                if(message instanceof ModuleShutdownRequest) {
                    final ModuleShutdownRequest msr = (ModuleShutdownRequest) message;
                    reply(msr, new ModuleShutdownResponse(toDto(), msr.getMessageId()));
                    break;
                }

                try (AutoCloseable messageId = MDC.putCloseable("messageId", message.getMessageId().getValue().toString())) {
                    if(message instanceof Request)
                        handleRequest((Request) message);
                    else if(message instanceof Response)
                        handleResponse((Response) message);
                    else
                        handle(message);
                }
            } catch (final Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    private void handleRequest(final Request message) {
        services.stream()
                .filter(service -> service.canHandle(message))
                .findFirst()
                .ifPresent(service -> service.handle(message));
    }

    private void handleResponse(final Response message) {
        final ServiceClient<?, ?> serviceClient = outstandingServiceRequests.remove(message.getRequestMessageId());
        if(serviceClient != null)
            serviceClient.handle(message);
    }

    private String[] getServiceQueueNames() {
        return serviceQueues.toArray(new String[] {});
    }

    public String getInstanceQueueName() {
        return "ModuleInstance|" + instanceId.toString();
    }

    /* ******************************* TEAR-DOWN ******************************* */
    private void cleanup() {
        getInjector().getInstance(Session.class).close();
        getInjector().getInstance(Cluster.class).close();
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
        return getInjector().getInstance(MessageQueueManager.class);
    }

    protected T getSettings() {
        return settings;
    }

    public ConfigurationProvider<T> getGlobalConfigProvider() {
        return getInjector().getInstance(ConfigurationProvider.class);
    }

    public UserConfigurationProvider<U> getUserConfigProvider() {
        return getInjector().getInstance(UserConfigurationProvider.class);
    }

    protected KeyValueStore getTemporaryKeyValueStore() {
        return getInjector().getInstance(Key.get(KeyValueStore.class, TemporaryModule.class));
    }

    public KeyValueStore getTemporaryGlobalKeyValueStore() {
        return getInjector().getInstance(Key.get(KeyValueStore.class, TemporaryGlobal.class));
    }

    public KeyValueStore getPersistentKeyValueStore() {
        return getInjector().getInstance(Key.get(KeyValueStore.class, PersistentModule.class));
    }

    protected KeyValueStore getPersistentGlobalKeyValueStore() {
        return getInjector().getInstance(Key.get(KeyValueStore.class, PersistentGlobal.class));
    }

    public ModuleRegistry getModuleRegistry() {
        return getInjector().getInstance(ModuleRegistry.class);
    }

    protected UUID getInstanceId() {
        return instanceId;
    }

    public MappingManager getMappingManager() {
        return getInjector().getInstance(MappingManager.class);
    }

    public DAOManager getDaoManager() {
        return getInjector().getInstance(DAOManager.class);
    }

    protected Deduplicator getDeduplicator() {
        return getInjector().getInstance(Deduplicator.class);
    }

    public CuratorFramework getCuratorFramework() {
        return getInjector().getInstance(CuratorFramework.class);
    }

    protected I18n getI18n() {
        return getInjector().getInstance(I18n.class);
    }

    public RedissonClient getRedisson() {
        return getInjector().getInstance(RedissonClient.class);
    }

    protected String getConfigFile() {
        return configFile;
    }

    public TwitchApi getTwitchApi() {
        return getInjector().getInstance(TwitchApi.class);
    }

    public DisplayNameService getDisplayNameService() {
        return getInjector().getInstance(DisplayNameService.class);
    }

    public Injector getInjector() {
        if(injector == null)
            injector = Guice.createInjector(new GuiceModule<>(settings, this));
        return injector;
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

    public T getGlobalConfiguration() {
        return getGlobalConfigProvider().getConfiguration();
    }

    public U getConfiguration(final Tenant tenant) {
        return getUserConfigProvider().getConfiguration(tenant);
    }

    public U getConfiguration(final ChannelGroup channelGroup) {
        return getUserConfigProvider().getConfiguration(channelGroup);
    }

    public U getConfiguration(final Channel channel) {
        return getUserConfigProvider().getConfiguration(channel);
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
        final ThirdPartyCredential thirdPartyCredential = getInjector().getInstance(DAOThirdPartyCredential.class).get(key);
        if(thirdPartyCredential == null)
            return null;
        return thirdPartyCredential.credentialAsByteArray();
    }

    public byte[] requireCredential(final String key) {
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

    public void registerService(final String serviceQueue, final Service<?, ?> service) {
        this.serviceQueues.add(serviceQueue);
        this.services.add(service);
    }

    public void unregisterService(final String serviceQueue, final Service<?, ?> service) {
        this.serviceQueues.remove(serviceQueue);
        this.services.remove(service);
    }

    public void expectResponseTo(final tv.v1x1.common.dto.core.UUID messageId, final ServiceClient<?, ?> serviceClient) {
        outstandingServiceRequests.put(messageId, serviceClient);
    }

    public void clearResponseTo(final tv.v1x1.common.dto.core.UUID messageId) {
        outstandingServiceRequests.remove(messageId);
    }

    /* ******************************* PRIVATE METHODS ******************************* */
    public Class<T> getGlobalConfigurationClass() {
        return Generics.getTypeParameter(getClass(), GlobalConfiguration.class);
    }

    public Class<U> getUserConfigurationClass() {
        return Generics.getTypeParameter(getClass(), UserConfiguration.class);
    }

    private void updateConfigurationDefinitions() {
        final DAOConfigurationDefinition daoConfigurationDefinition = getDaoManager().getDaoConfigurationDefinition();
        final GlobalConfigurationDefinition globalConfigurationDefinition = ConfigScanner.scanGlobal(getGlobalConfigurationClass());
        if(globalConfigurationDefinition != null)
            daoConfigurationDefinition.put(globalConfigurationDefinition.toDB());
        final UserConfigurationDefinition userConfigurationDefinition = ConfigScanner.scanUser(getUserConfigurationClass());
        if(userConfigurationDefinition != null)
            daoConfigurationDefinition.put(userConfigurationDefinition.toDB());
    }

    private void registerModulePermissions() {
        PermissionScanner.scanClass(this);
    }

    /* ******************************* LANGUAGE ******************************* */
    private void registerGlobalMessages() {
        final tv.v1x1.common.dto.core.Module module = new tv.v1x1.common.dto.core.Module("_GLOBAL_");
        I18n.registerDefault(module, "generic.error",
                "%commander%, you broke me! :'[ I've told my keepers about this. Want to make a report? " +
                        "Send them a message with this info: Error ID: %errorId%. Type: %message%");
        I18n.registerDefault(module, "generic.noperms",
                "%commander%, sorry, looks like you don't have permission to do that.");
        I18n.registerDefault(module, "generic.twitchapi.error",
                "Sorry, there was a problem talking with the Twitch API. BibleThump If this persists, please con" +
                        "tact the Bot Operators");
        I18n.registerDefault(module, "generic.invalid.user",
                "%commander%, \"%input%\" is not a valid username here");
    }

    private void registerModuleMessages() {
        I18nScanner.scanClass(this);
    }
}
