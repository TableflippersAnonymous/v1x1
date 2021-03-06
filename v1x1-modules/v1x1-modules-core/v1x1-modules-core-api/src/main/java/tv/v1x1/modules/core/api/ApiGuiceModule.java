package tv.v1x1.modules.core.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.services.cache.CacheManager;
import tv.v1x1.common.services.coordination.ModuleRegistry;
import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.persistence.ConfigurationCacheManager;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.persistence.KeyValueStore;
import tv.v1x1.common.services.pubsub.TopicManager;
import tv.v1x1.common.services.queue.MessageQueueManager;
import tv.v1x1.common.services.spotify.SpotifyApi;
import tv.v1x1.common.services.state.DiscordDisplayNameService;
import tv.v1x1.common.services.state.TwitchDisplayNameService;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.modules.core.api.auth.Authorizer;

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
    public SpotifyApi provideSpotifyApi() {
        return new SpotifyApi(new String(apiModule.requireCredential("Common|Spotify|ClientId")),
                null, new String(apiModule.requireCredential("Common|Spotify|ClientSecret")),
                new String(apiModule.requireCredential("Common|Spotify|RedirectUri")));
    }

    @Provides
    public DiscordApi provideDiscordApi() {
        return apiModule.getInjector().getInstance(DiscordApi.class);
    }

    @Provides
    public KeyValueStore provideKeyValueStore() {
        return apiModule.getTemporaryKeyValueStore();
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

    @Provides
    public DiscordDisplayNameService provideDiscordDisplayNameService() {
        return apiModule.getInjector().getInstance(DiscordDisplayNameService.class);
    }

    @Provides
    public DAOGlobalUser provideDaoGlobalUser() {
        return apiModule.getInjector().getInstance(DAOGlobalUser.class);
    }

    @Provides
    public TopicManager provideTopicManager() {
        return apiModule.getInjector().getInstance(TopicManager.class);
    }
}
