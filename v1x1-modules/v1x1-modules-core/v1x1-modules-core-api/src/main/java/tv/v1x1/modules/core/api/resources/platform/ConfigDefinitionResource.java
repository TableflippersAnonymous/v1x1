package tv.v1x1.modules.core.api.resources.platform;

import com.google.inject.Inject;
import io.dropwizard.jersey.caching.CacheControl;
import tv.v1x1.common.dto.db.ChannelConfigurationDefinition;
import tv.v1x1.common.dto.db.GlobalConfigurationDefinition;
import tv.v1x1.common.dto.db.TenantConfigurationDefinition;
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
      /tenant - GET: list of modules with TenantConfigurationDefinitions.
        /{module} - GET: TenantConfigurationDefinition for module.
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

    @Path("/channel")
    @GET
    @CacheControl(maxAge = 15, maxAgeUnit = TimeUnit.MINUTES)
    public ApiList<String> listChannelConfigDefinitions() {
        final List<String> ret = new ArrayList<>();
        for(final ChannelConfigurationDefinition def : daoManager.getDaoConfigurationDefinition().getAllChannel()) {
            ret.add(def.getName());
        }
        return new ApiList<>(ret);
    }

    @Path("/channel/{name}")
    @GET
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.HOURS)
    public ConfigurationDefinition getChannelConfigurationDefinition(@PathParam("name") final String name) {
        final ChannelConfigurationDefinition definition = daoManager.getDaoConfigurationDefinition().getChannel(name);
        if(definition == null)
            throw new NotFoundException();
        return ConfigurationDefinition.fromCore(definition.toCore());
    }

    @Path("/tenant")
    @GET
    @CacheControl(maxAge = 15, maxAgeUnit = TimeUnit.MINUTES)
    public ApiList<String> listTenantConfigDefinitions() {
        final List<String> ret = new ArrayList<>();
        for(final TenantConfigurationDefinition def : daoManager.getDaoConfigurationDefinition().getAllTenant()) {
            ret.add(def.getName());
        }
        return new ApiList<>(ret);
    }

    @Path("/tenant/{name}")
    @GET
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.HOURS)
    public ConfigurationDefinition getTenantConfigurationDefinition(@PathParam("name") final String name) {
        final TenantConfigurationDefinition definition = daoManager.getDaoConfigurationDefinition().getTenant(name);
        if(definition == null)
            throw new NotFoundException();
        return ConfigurationDefinition.fromCore(definition.toCore());
    }

    @Path("/global")
    @GET
    @CacheControl(maxAge = 15, maxAgeUnit = TimeUnit.MINUTES)
    public ApiList<String> listGlobalConfigDefinitions() {
        final List<String> ret = new ArrayList<>();
        for(final GlobalConfigurationDefinition def : daoManager.getDaoConfigurationDefinition().getAllGlobal()) {
            ret.add(def.getName());
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
