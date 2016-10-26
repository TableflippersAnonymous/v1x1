package tv.v1x1.modules.core.api.resources.globaluser;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by naomi on 10/26/2016.
 */
/*
  /global-users
    /{platform}
      /{username} - GET: global user id
    /{userid} - GET: list of endpoints
 */
@Path("/api/v1/global-users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GlobalUsersResource {
    @Path("/{global_user_id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    @GET
    public List<String> listGlobalUserEndpoints(@PathParam("global_user_id") final String globalUserId) {
        return null; //TODO
    }

    @Path("/{platform: [a-z]+}/{id}")
    @GET
    public String getGlobalUserId(@PathParam("platform") final String platform, @PathParam("id") final String id) {
        return null; //TODO
    }
}
