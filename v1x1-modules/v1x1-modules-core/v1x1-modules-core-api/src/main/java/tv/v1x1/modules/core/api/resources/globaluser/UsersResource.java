package tv.v1x1.modules.core.api.resources.globaluser;

import tv.v1x1.modules.core.api.api.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
  /global-users
    /{userid}
      /users - GET: list of platforms
        /{platform} - GET: list of users on platform
          /{user} - GET: user object; PUT: link a user; DELETE: unlink a user
 */
@Path("/api/v1/global-users/{global_user_id}/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {
    @GET
    public List<String> listPlatforms(@PathParam("global_user_id") final String globalUserId) {
        return null; //TODO
    }

    @Path("/{platform}")
    @GET
    public List<String> listUsers(@PathParam("global_user_id") final String globalUserId, @PathParam("platform") final String platform) {
        return null; //TODO
    }

    @Path("/{platform}/{user_id}")
    @GET
    public User getUser(@PathParam("global_user_id") final String globalUserId, @PathParam("platform") final String platform,
                        @PathParam("user_id") final String userId) {
        return null; //TODO
    }

    @Path("/{platform}/{user_id}")
    @PUT
    public User linkUser(@PathParam("global_user_id") final String globalUserId, @PathParam("platform") final String platform,
                         @PathParam("user_id") final String userId, final User user) {
        return null; //TODO
    }

    @Path("/{platform}/{user_id}")
    @DELETE
    public Response unlinkUser(@PathParam("global_user_id") final String globalUserId, @PathParam("platform") final String platform,
                               @PathParam("user_id") final String userId) {
        return null; //TODO
    }
}
