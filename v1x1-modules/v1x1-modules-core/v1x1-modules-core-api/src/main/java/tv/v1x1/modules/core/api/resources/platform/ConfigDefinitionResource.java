package tv.v1x1.modules.core.api.resources.platform;

import tv.v1x1.common.dto.db.GlobalConfigurationDefinition;
import tv.v1x1.common.dto.db.TenantConfigurationDefinition;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.modules.core.api.api.ConfigurationDefinition;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naomi on 10/24/2016.
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

    @Path("/tenant")
    @GET
    public List<String> listTenantConfigDefinitions() {
        final List<String> ret = new ArrayList<>();
        for(final TenantConfigurationDefinition def : daoManager.getDaoConfigurationDefinition().getAllTenant()) {
            ret.add(def.getName());
        }
        return ret;
    }

    @Path("/tenant/{name}")
    @GET
    public ConfigurationDefinition getTenantConfigurationDefinition(@PathParam("name") final String name) {
        return null; //TODO
    }

    @Path("/global")
    @GET
    public List<String> listGlobalConfigDefinitions() {
        final List<String> ret = new ArrayList<>();
        for(final GlobalConfigurationDefinition def : daoManager.getDaoConfigurationDefinition().getAllGlobal()) {
            ret.add(def.getName());
        }
        return ret;
    }

    @Path("/tenant/{name}")
    @GET
    public ConfigurationDefinition getGlobalConfigurationDefinition(@PathParam("name") final String name) {
        return null; //TODO
    }
}
