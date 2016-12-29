package tv.v1x1.modules.core.api.resources.tenant;

import com.google.inject.Inject;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.modules.core.api.api.Channel;
import tv.v1x1.modules.core.api.auth.Authorizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Naomi
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
    private final DAOTenant daoTenant;
    private final Authorizer authorizer;

    @Inject
    public ChannelsResource(final DAOManager daoManager, final Authorizer authorizer) {
        this.daoTenant = daoManager.getDaoTenant();
        this.authorizer = authorizer;
    }

    @GET
    public List<String> listPlatforms(@PathParam("tenant_id") final String tenantId) {
        return null; //TODO
    }

    @Path("/{platform}")
    @GET
    public List<String> listChannels(@HeaderParam("Authorization") final String authorization,
                                     @PathParam("tenant_id") final String tenantId, @PathParam("platform") final String platformStr) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.tenants.read");
        final Platform platform = getDtoPlatform(platformStr);
        return tenant.getEntries().stream().filter(entry -> entry.getPlatform().equals(platform))
                .map(Tenant.Entry::getChannelId).collect(Collectors.toList());
    }

    @Path("/{platform}/{channel_id}")
    @GET
    public Channel getChannel(@HeaderParam("Authorization") final String authorization,
                              @PathParam("tenant_id") final String tenantId,
                              @PathParam("platform") final String platformStr,
                              @PathParam("channel_id") final String channelId) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.tenants.read");
        final Platform platform = getDtoPlatform(platformStr);
        return getDtoChannel(tenant, platform, channelId);
    }

    @Path("/{platform}/{channel_id}")
    @PUT
    public Channel linkChannel(@HeaderParam("Authorization") final String authorization,
                               @PathParam("tenant_id") final String tenantId,
                               @PathParam("platform") final String platform,
                               @PathParam("channel_id") final String channelId,
                               final Channel channel) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.tenants.link");
        return null; // TODO I have no idea what I'm doing
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
    public Response postMessage(@HeaderParam("Authorization") final String authorization,
                                @PathParam("tenant_id") final String tenantId,
                                @PathParam("platform") final String platformStr,
                                @PathParam("channel_id") final String channelId,
                                final String message) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.tenants.message");
        final Platform platform = getDtoPlatform(platformStr);
        final Channel apiChannel = getDtoChannel(tenant, platform, channelId);
        final tv.v1x1.common.dto.core.Channel coreChannel = getCoreChannel(apiChannel, platform);
        Chat.message(null, coreChannel, message);
        return Response.ok().build();
    }

    private Tenant getDtoTenant(final String tenantIdStr) {
        final UUID tenantId;
        try {
            tenantId = UUID.fromString(tenantIdStr);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Tenant ID is invalid");
        }
        final Tenant tenant = daoTenant.getById(tenantId);
        if(tenant == null)
            throw new NotFoundException("Tenant not found");
        return tenant;
    }

    private Platform getDtoPlatform(final String platformStr) {
        try {
            return Platform.valueOf(platformStr.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new NotFoundException("Platform is invalid");
        }
    }

    private Channel getDtoChannel(final Tenant tenant, final Platform platform, final String channelId) {
        final Tenant.Entry channelEntry = tenant.getEntries().stream().filter(entry -> entry.getPlatform().equals(platform))
                .filter(entry -> entry.getChannelId().equals(channelId)).findFirst()
                .orElseThrow(() -> new NotFoundException("Channel not found"));
       return new Channel(tenant.getId(), channelEntry.getPlatform(), channelEntry.getDisplayName(), channelEntry.getChannelId());
    }

    private tv.v1x1.common.dto.core.Channel getCoreChannel(final Channel apiChannel, final Platform platform) {
        return null; // TODO: Programmatically return the correct type of t.v.c.d.core.Channel
    }
}
