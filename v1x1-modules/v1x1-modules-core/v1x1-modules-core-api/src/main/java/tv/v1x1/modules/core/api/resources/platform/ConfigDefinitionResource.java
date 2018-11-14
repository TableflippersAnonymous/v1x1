package tv.v1x1.modules.core.api.resources.platform;

import com.google.inject.Inject;
import io.dropwizard.jersey.caching.CacheControl;
import tv.v1x1.common.dto.db.GlobalConfigurationDefinition;
import tv.v1x1.common.dto.db.UserConfigurationDefinition;
import tv.v1x1.common.scanners.permission.DefaultGroup;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.modules.core.api.ApiModule;
import tv.v1x1.modules.core.api.api.rest.ApiList;
import tv.v1x1.modules.core.api.api.rest.ConfigurationDefinition;
import tv.v1x1.modules.core.api.api.rest.I18nDefinition;
import tv.v1x1.modules.core.api.api.rest.PermissionDefinition;
import tv.v1x1.modules.core.api.services.ApiDataProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by cobi on 10/24/2016.
 */
/*
  /platform
    /config-definitions
      /global - GET: list of modules with GlobalConfigurationDefinitions.
        /{module} - GET: GlobalConfigurationDefinition for module.
      /user - GET: list of modules with UserConfigurationDefinitions.
        /{module} - GET: UserConfigurationDefinition for module.
 */
@Path("/api/v1/platform/config-definitions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConfigDefinitionResource {
    private final DAOManager daoManager;
    private final ApiDataProvider dataProvider;

    @Inject
    public ConfigDefinitionResource(final DAOManager daoManager, final ApiModule module) {
        this.daoManager = daoManager;
        this.dataProvider = new ApiDataProvider(daoManager, module);
    }

    @Path("/user")
    @GET
    @CacheControl(maxAge = 15, maxAgeUnit = TimeUnit.MINUTES)
    public ApiList<ConfigurationDefinition> listUserConfigDefinitions() {
        return new ApiList<>(dataProvider.getUserConfigDefinitions());
    }

    @Path("/user/{name}")
    @GET
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.HOURS)
    public ConfigurationDefinition getUserConfigurationDefinition(@PathParam("name") final String name) {
        final UserConfigurationDefinition definition = daoManager.getDaoConfigurationDefinition().getUser(name);
        if(definition == null)
            throw new NotFoundException();
        return ConfigurationDefinition.fromCore(definition.toCore());
    }

    @Path("/global")
    @GET
    @CacheControl(maxAge = 15, maxAgeUnit = TimeUnit.MINUTES)
    public ApiList<ConfigurationDefinition> listGlobalConfigDefinitions() {
        return new ApiList<>(dataProvider.getGlobalConfigDefinitions());
    }

    @Path("/global/{name}")
    @GET
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.HOURS)
    public ConfigurationDefinition getGlobalConfigurationDefinition(@PathParam("name") final String name) {
        final GlobalConfigurationDefinition definition = daoManager.getDaoConfigurationDefinition().getGlobal(name);
        if(definition == null)
            throw new NotFoundException();
        return ConfigurationDefinition.fromCore(definition.toCore());
    }

    @Path("/permission")
    @GET
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.HOURS)
    public ApiList<PermissionDefinition> listPermissionDefinitions() {
        return new ApiList<>(StreamSupport.stream(daoManager.getDaoPermissionDefinition().getAll().spliterator(), false).map(
                permissionDefinition -> new PermissionDefinition(
                        permissionDefinition.getName(),
                        permissionDefinition.getVersion(),
                        permissionDefinition.getEntries().stream().map(
                                permissionEntry -> new PermissionDefinition.Entry(
                                        permissionEntry.getNode(),
                                        permissionEntry.getDisplayName(),
                                        permissionEntry.getDescription(),
                                        permissionEntry.getDefaultGroups().stream().map(DefaultGroup::getGroupName).collect(Collectors.toList())
                                )
                        ).collect(Collectors.toList())
                )
        ).collect(Collectors.toList()));
    }

    @Path("/i18n")
    @GET
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.HOURS)
    public ApiList<I18nDefinition> listI18nDefinitions() {
        return new ApiList<>(StreamSupport.stream(daoManager.getDaoI18nDefinition().getAll().spliterator(), false).map(
                i18nDefinition -> new I18nDefinition(
                        i18nDefinition.getName(),
                        i18nDefinition.getVersion(),
                        i18nDefinition.getEntries().stream().map(
                                i18nEntry -> new I18nDefinition.Entry(
                                        i18nEntry.getKey(),
                                        i18nEntry.getMessage(),
                                        i18nEntry.getDisplayName(),
                                        i18nEntry.getDescription()
                                )
                        ).collect(Collectors.toList())
                )
        ).collect(Collectors.toList()));
    }
}
