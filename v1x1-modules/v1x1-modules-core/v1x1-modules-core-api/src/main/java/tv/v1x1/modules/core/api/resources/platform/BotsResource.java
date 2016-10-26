package tv.v1x1.modules.core.api.resources.platform;

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
  /platform
    /bots
      /{platform} - GET: list of bots that v1x1 is using on platform.
        /{botname} - GET: list of channels bot is in
 */
@Path("/api/v1/platform/bots")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BotsResource {
    @Path("/{platform}")
    @GET
    public List<String> listBotsOnPlatform(@PathParam("platform") final String platform) {
        return null; //TODO
    }

    @Path("/{platform}/{botname}")
    @GET
    public List<String> listChannelsBotOn(@PathParam("platform") final String platform, @PathParam("botname") final String botName) {
        return null; //TODO
    }
}
