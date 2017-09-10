package tv.v1x1.modules.core.api.resources.platform;

import com.google.inject.Inject;
import io.dropwizard.jersey.caching.CacheControl;
import tv.v1x1.common.dto.db.GlobalConfigurationDefinition;
import tv.v1x1.common.dto.db.UserConfigurationDefinition;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.modules.core.api.api.rest.ApiList;
import tv.v1x1.modules.core.api.api.rest.ConfigurationDefinition;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Inject
    public ConfigDefinitionResource(final DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Path("/user")
    @GET
    @CacheControl(maxAge = 15, maxAgeUnit = TimeUnit.MINUTES)
    public ApiList<ConfigurationDefinition> listUserConfigDefinitions() {
        final List<ConfigurationDefinition> ret = new ArrayList<>();
        for(final UserConfigurationDefinition def : daoManager.getDaoConfigurationDefinition().getAllUser()) {
            ret.add(ConfigurationDefinition.fromCore(def.toCore()));
        }
        return new ApiList<>(ret);
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
        final List<ConfigurationDefinition> ret = new ArrayList<>();
        for(final GlobalConfigurationDefinition def : daoManager.getDaoConfigurationDefinition().getAllGlobal()) {
            ret.add(ConfigurationDefinition.fromCore(def.toCore()));
        }
        return new ApiList<>(ret);
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
}
