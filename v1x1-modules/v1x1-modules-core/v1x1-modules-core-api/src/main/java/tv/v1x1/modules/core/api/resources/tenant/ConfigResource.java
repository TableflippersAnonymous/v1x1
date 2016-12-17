package tv.v1x1.modules.core.api.resources.tenant;

import com.google.inject.Inject;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.modules.core.api.api.Configuration;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by cobi on 10/24/2016.
 */
/*
  /tenants
    /{tenant}
      /config
        /{module} - GET: TenantConfiguration for module; PUT: Update TenantConfiguration for module.
 */
@Path("/api/v1/tenants/{tenant}/config")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConfigResource {
    private final DAOManager daoManager;

    @Inject
    public ConfigResource(final DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Path("/{module}")
    @GET
    public Configuration getConfig(@PathParam("tenant") final String tenantId, @PathParam("module") final String moduleName) {
        return null; //TODO
    }

    @Path("/{module}")
    @PUT
    public Configuration putConfig(@PathParam("tenant") final String tenantId, @PathParam("module") final String moduleName,
                                   final Configuration configuration) {
        return null; //TODO
    }
}
