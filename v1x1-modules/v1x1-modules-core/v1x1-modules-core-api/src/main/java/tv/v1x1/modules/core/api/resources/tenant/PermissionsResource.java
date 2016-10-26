package tv.v1x1.modules.core.api.resources.tenant;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by naomi on 10/26/2016.
 */
/*
  /tenants
    /{tenant}
      /permissions
        /{user} - GET: List permissions; POST: Add permission
          /{permission} - DELETE: Remove permission
 */
@Path("/api/1/tenants/{tenant}/permissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PermissionsResource {
    @Path("/{user}")
    @GET
    public List<String> listPermissionsForUser(@PathParam("tenant") final String tenantId, @PathParam("user") final String userId) {
        return null; //TODO
    }

    @Path("/{user}")
    @POST
    public List<String> addPermissionToUser(@PathParam("tenant") final String tenantId, @PathParam("user") final String userId,
                                            final String permissionNode) {
        return null; //TODO
    }

    @Path("/{user}/{permission}")
    @DELETE
    public Response removePermissionFromuser(@PathParam("tenant") final String tenantId, @PathParam("user") final String userId,
                                             @PathParam("permission") final String permissionNode) {
        return null; //TODO
    }
}
