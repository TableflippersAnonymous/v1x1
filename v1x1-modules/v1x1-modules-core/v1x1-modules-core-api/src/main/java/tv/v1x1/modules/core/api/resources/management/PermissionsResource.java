package tv.v1x1.modules.core.api.resources.management;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by cobi on 10/26/2016.
 */
/*
  /management
    /permissions - GET: List all global permissions
      /{user} - GET: list permissions for user; POST: grant permission to user
        /{permission} - DELETE: revoke permission from user
 */
@Path("/api/v1/management/permissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PermissionsResource {
    @GET
    public List<String> listUsers() {
        return null; //TODO
    }

    @Path("/{user}")
    @GET
    public List<String> listPermissions(@PathParam("user") final String userId) {
        return null; //TODO
    }

    @Path("/{user}")
    @POST
    public List<String> grantPermission(@PathParam("user") final String userId, final String permissionNode) {
        return null; //TODO
    }

    @Path("/{user}/{permission}")
    @DELETE
    public Response revokePermission(@PathParam("user") final String userId, @PathParam("permission") final String permissionNode) {
        return null; //TODO
    }
}
