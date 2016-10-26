package tv.v1x1.modules.core.api.resources.tenant;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by cobi on 10/26/2016.
 */
/*
  /tenants - GET: list of tenants you are allowed to control.
    /{platform}
      /{channel} - GET: tenant-id
    /{tenant} - GET: list of endpoints
 */
@Path("/api/v1/tenants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TenantsResource {
    @GET
    public List<String> listTenantsForUser() {
        return null; //TODO
    }

    @Path("/{tenant_id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    @GET
    public List<String> listTenantEndpoints(@PathParam("tenant_id") final String tenantId) {
        return null; //TODO
    }

    @Path("/{platform: [a-z]+}/{id}")
    @GET
    public String getTenantId(@PathParam("platform") final String platform, @PathParam("id") final String id) {
        return null; //TODO
    }
}
