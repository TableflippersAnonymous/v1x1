package tv.v1x1.modules.core.api.resources.meta;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by cobi on 10/26/2016.
 */
/*
  /meta
    /self - GET: redirect to /global-users/{userid} for currently logged in user
 */
@Path("/api/v1/meta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MetaResource {
    @Path("/self")
    @GET
    public Response getSelf() {
        return null; //TODO
    }
}
