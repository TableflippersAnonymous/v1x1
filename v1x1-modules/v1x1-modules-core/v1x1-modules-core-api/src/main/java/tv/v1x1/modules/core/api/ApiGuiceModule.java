package tv.v1x1.modules.core.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import tv.v1x1.common.services.cache.CacheManager;
import tv.v1x1.common.services.coordination.ModuleRegistry;
import tv.v1x1.common.services.persistence.ConfigurationCacheManager;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.persistence.KeyValueStore;
import tv.v1x1.common.services.queue.MessageQueueManager;
import tv.v1x1.common.services.state.TwitchDisplayNameService;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.modules.core.api.auth.Authorizer;
import tv.v1x1.modules.core.api.resources.ws.PubsubResource;

/**
 * Created by cobi on 10/24/2016.
 */
public class ApiGuiceModule extends AbstractModule {
    private final ApiModule apiModule;

    public ApiGuiceModule(final ApiModule module) {
        this.apiModule = module;
    }

    @Override
    protected void configure() {
        //requestStaticInjection(PubsubResource.class);
    }

    @Provides
    public ApiModule provideApiModule() {
        return apiModule;
    }

    @Provides
    public DAOManager provideDAOManager() {
        return apiModule.getDaoManager();
    }

    @Provides
    public Authorizer provideAuthorizer() {
        return new Authorizer(apiModule.getDaoManager().getDaoTenantGroup(), apiModule.getDaoManager().getDaoTenant(), apiModule.getDaoManager().getDaoThirdPartyCredential(), apiModule.getDaoManager().getDaoGlobalUser());
    }

    @Provides
    public TwitchApi provideTwitchApi() {
        return apiModule.getTwitchApi();
    }

    @Provides
    public KeyValueStore provideKeyValueStore() {
        return apiModule.getTemporaryGlobalKeyValueStore();
    }

    @Provides
    public ModuleRegistry provideModuleRegistry() {
        return apiModule.getModuleRegistry();
    }

    @Provides
    public MessageQueueManager provideMessageQueueManager() {
        return apiModule.getMessageQueueManager();
    }

    @Provides
    public ConfigurationCacheManager provideConfigurationCacheManager() {
        return apiModule.getInjector().getInstance(ConfigurationCacheManager.class);
    }

    @Provides
    public CacheManager provideCacheManager() {
        return apiModule.getInjector().getInstance(CacheManager.class);
    }

    @Provides
    public TwitchDisplayNameService provideTwitchDisplayNameService() {
        return apiModule.getInjector().getInstance(TwitchDisplayNameService.class);
    }
}
