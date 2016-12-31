package tv.v1x1.modules.core.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import tv.v1x1.common.services.coordination.ModuleRegistry;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.persistence.KeyValueStore;
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
    public KeyValueStore provideKeyValueStore() {
        return apiModule.getTemporaryGlobalKeyValueStore();
    }

    @Provides
    public ModuleRegistry provideModuleRegistry() {
        return apiModule.getModuleRegistry();
    }
}
