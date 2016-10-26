package tv.v1x1.modules.core.api.resources.tenant;

import tv.v1x1.modules.core.api.api.Channel;

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
      /channels - GET: list of platforms
        /{platform} - GET: list of channels with that platform
          /{channel} - GET: channel object; PUT: link a channel; DELETE: unlink a channel
            /state - GET: whether bot is in channel or not; PUT: change whether bot should be in channel or not
            /message - POST: send message as bot to channel
 */
@Path("/api/v1/tenants/{tenant_id}/channels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChannelsResource {

    @GET
    public List<String> listPlatforms(@PathParam("tenant_id") final String tenantId) {
        return null; //TODO
    }

    @Path("/{platform}")
    @GET
    public List<String> listChannels(@PathParam("tenant_id") final String tenantId, @PathParam("platform") final String platform) {
        return null; //TODO
    }

    @Path("/{platform}/{channel_id}")
    @GET
    public Channel getChannel(@PathParam("tenant_id") final String tenantId, @PathParam("platform") final String platform,
                              @PathParam("channel_id") final String channelId) {
        return null; //TODO
    }

    @Path("/{platform}/{channel_id}")
    @PUT
    public Channel linkChannel(@PathParam("tenant_id") final String tenantId, @PathParam("platform") final String platform,
                               @PathParam("channel_id") final String channelId, final Channel channel) {
        return null; //TODO
    }

    @Path("/{platform}/{channel_id}")
    @DELETE
    public Response unlinkChannel(@PathParam("tenant_id") final String tenantId, @PathParam("platform") final String platform,
                                  @PathParam("channel_id") final String channelId) {
        return null; //TODO
    }

    @Path("/{platform}/{channel_id}/state")
    @GET
    public String getState(@PathParam("tenant_id") final String tenantId, @PathParam("platform") final String platform,
                           @PathParam("channel_id") final String channelId) {
        return null; //TODO
    }

    @Path("/{platform}/{channel_id}/state")
    @PUT
    public String putState(@PathParam("tenant_id") final String tenantId, @PathParam("platform") final String platform,
                           @PathParam("channel_id") final String channelId, final String newState) {
        return null; //TODO
    }

    @Path("/{platform}/{channel_id}/message")
    @POST
    public Response postMessage(@PathParam("tenant_id") final String tenantId, @PathParam("platform") final String platform,
                                @PathParam("channel_id") final String channelId, final String message) {
        return null; //TODO
    }
}
