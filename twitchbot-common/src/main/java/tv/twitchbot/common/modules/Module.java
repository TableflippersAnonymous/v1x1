package tv.twitchbot.common.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.dropwizard.util.Generics;
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
import tv.twitchbot.common.dto.core.Tenant;
import tv.twitchbot.common.dto.messages.Message;
import tv.twitchbot.common.dto.messages.Request;
import tv.twitchbot.common.dto.messages.Response;
import tv.twitchbot.common.dto.messages.requests.ModuleShutdownRequest;
import tv.twitchbot.common.dto.messages.responses.ModuleShutdownResponse;
import tv.twitchbot.common.rpc.client.ServiceClient;
import tv.twitchbot.common.services.persistence.KeyValueStore;
import tv.twitchbot.common.services.persistence.TemporaryKeyValueStoreImpl;
import tv.twitchbot.common.services.persistence.TenantKeyValueStoreImpl;
import tv.twitchbot.common.services.queue.MessageQueue;
import tv.twitchbot.common.services.queue.MessageQueueManager;
import tv.twitchbot.common.services.queue.MessageQueueManagerImpl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cobi on 10/4/16.
 */
public abstract class Module<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration> {
    private T settings;
    private U globalConfig;
    private MessageQueueManager messageQueueManager;

    private KeyValueStore temporaryKeyValueStore;
    private KeyValueStore temporaryGlobalKeyValueStore;

    private KeyValueStore persistentKeyValueStore;
    private KeyValueStore persistentGlobalKeyValueStore;

    private Map<Class<? extends ServiceClient>, ServiceClient> serviceClientMap = new ConcurrentHashMap<>();

    protected KeyValueStore getTemporaryKeyValueStore() {
        return temporaryKeyValueStore;
    }

    protected KeyValueStore getTemporaryGlobalKeyValueStore() {
        return temporaryGlobalKeyValueStore;
    }

    protected KeyValueStore getTemporaryTenantKeyValueStore(Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, temporaryKeyValueStore);
    }

    protected KeyValueStore getTemporaryGlobalTenantKeyValueStore(Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, temporaryGlobalKeyValueStore);
    }

    protected KeyValueStore getPersistentKeyValueStore() {
        return persistentKeyValueStore;
    }

    protected KeyValueStore getPersistentGlobalKeyValueStore() {
        return persistentGlobalKeyValueStore;
    }

    protected KeyValueStore getPersistentTenantKeyValueStore(Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, persistentKeyValueStore);
    }

    protected KeyValueStore getPersistentGlobalTenantKeyValueStore(Tenant tenant) {
        return new TenantKeyValueStoreImpl(tenant, persistentGlobalKeyValueStore);
    }

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

    public abstract String getName();

    protected abstract void handle(Message message);

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
    }

    private Class<T> getSettingsClass() {
        return Generics.getTypeParameter(getClass(), ModuleSettings.class);
    }

    private Class<U> getGlobalConfigurationClass() {
        return Generics.getTypeParameter(getClass(), GlobalConfiguration.class);
    }

    private Class<V> getTenantConfigurationClass() {
        return Generics.getTypeParameter(getClass(), TenantConfiguration.class);
    }

    protected abstract void initialize();
    protected abstract void shutdown();

    private void run() throws Exception {
        MessageQueueManager mqm = getMessageQueueManager();
        MessageQueue mq = mqm.forName(getQueueName());
        for(;;) {
            try {
                Message message = mq.get();
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

    private void cleanup() {

    }

    public tv.twitchbot.common.dto.core.Module toDto() {
        return new tv.twitchbot.common.dto.core.Module(getName());
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
        return "inbound|module|" + module.getName();
    }
}
