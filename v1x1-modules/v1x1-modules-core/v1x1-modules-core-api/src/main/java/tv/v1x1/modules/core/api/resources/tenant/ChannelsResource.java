package tv.v1x1.modules.core.api.resources.tenant;

import com.google.inject.Inject;
import tv.v1x1.common.dao.DAOJoinedTwitchChannel;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.db.JoinedTwitchChannel;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.modules.core.api.ApiModule;
import tv.v1x1.modules.core.api.api.rest.ApiList;
import tv.v1x1.modules.core.api.api.rest.ApiPrimitive;
import tv.v1x1.modules.core.api.api.rest.Channel;
import tv.v1x1.modules.core.api.api.rest.ChannelGroup;
import tv.v1x1.modules.core.api.auth.Authorizer;

import javax.ws.rs.ClientErrorException;
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
import java.util.ArrayList;
import java.util.Optional;
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
            /config - GET: configuration; PUT: update configuration
 */
@Path("/api/v1/tenants/{tenant_id}/channels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChannelsResource {
    private final DAOTenant daoTenant;
    private final DAOJoinedTwitchChannel daoJoinedTwitchChannel;
    private final Authorizer authorizer;
    private final ApiModule module;

    @Inject
    public ChannelsResource(final DAOManager daoManager, final Authorizer authorizer, final ApiModule module) {
        this.daoTenant = daoManager.getDaoTenant();
        this.daoJoinedTwitchChannel = daoManager.getDaoJoinedTwitchChannel();
        this.authorizer = authorizer;
        this.module = module;
    }

    @GET
    public ApiList<String> listPlatforms(@HeaderParam("Authorization") final String authorization,
                                         @PathParam("tenant_id") final String tenantId) {
        final Tenant tenant = getTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId().getValue(), authorization).ensurePermission("api.tenants.read");
        return new ApiList<>(new ArrayList<>(daoTenant.getChannelGroups(tenant.toDB()).stream().map(tv.v1x1.common.dto.db.ChannelGroup::getPlatform)
                .map(Platform::name).map(String::toLowerCase).collect(Collectors.toSet())));
    }

    @Path("/{platform}")
    @GET
    public ApiList<String> listChannelGroups(@HeaderParam("Authorization") final String authorization,
                                             @PathParam("tenant_id") final String tenantId,
                                             @PathParam("platform") final String platformStr) {
        final Tenant tenant = getTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId().getValue(), authorization).ensurePermission("api.tenants.read");
        final Platform platform = getDtoPlatform(platformStr);
        return new ApiList<>(daoTenant.getChannelGroups(tenant.toDB()).stream()
                .filter(channelGroup -> channelGroup.getPlatform().equals(platform))
                .map(tv.v1x1.common.dto.db.ChannelGroup::getId).collect(Collectors.toList()));
    }

    @Path("/{platform}/{channel_group_id}")
    @GET
    public ChannelGroup getChannelGroup(@HeaderParam("Authorization") final String authorization,
                                        @PathParam("tenant_id") final String tenantId,
                                        @PathParam("platform") final String platformStr,
                                        @PathParam("channel_group_id") final String channelGroupId) {
        final Tenant tenant = getTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId().getValue(), authorization).ensurePermission("api.tenants.read");
        final Platform platform = getDtoPlatform(platformStr);
        return getDtoChannelGroup(tenant, platform, channelGroupId);
    }

    @Path("/{platform}/{channel_group_id}/{channel_id}")
    @GET
    public Channel getChannel(@HeaderParam("Authorization") final String authorization,
                              @PathParam("tenant_id") final String tenantId,
                              @PathParam("platform") final String platformStr,
                              @PathParam("channel_group_id") final String channelGroupId,
                              @PathParam("channel_id") final String channelId) {
        final Tenant tenant = getTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId().getValue(), authorization).ensurePermission("api.tenants.read");
        final Platform platform = getDtoPlatform(platformStr);
        return getDtoChannel(tenant, platform, channelGroupId, channelId);
    }

    @Path("/{platform}/{channel_id}")
    @PUT
    public Channel linkChannel(@HeaderParam("Authorization") final String authorization,
                               @PathParam("tenant_id") final String tenantId,
                               @PathParam("platform") final String platform,
                               @PathParam("channel_id") final String channelId,
                               final Channel channel) {
        final Tenant tenant = getTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId().getValue(), authorization).ensurePermission("api.tenants.link");
        return null; // TODO I have no idea what I'm doing
    }

    @Path("/{platform}/{channel_id}")
    @DELETE
    public Response unlinkChannel(@PathParam("tenant_id") final String tenantId, @PathParam("platform") final String platform,
                                  @PathParam("channel_id") final String channelId) {
        return null; //TODO
    }

    @Path("/{platform}/{channel_group_id}/state")
    @GET
    public ApiPrimitive<String> getState(@HeaderParam("Authorization") final String authorization,
                                         @PathParam("tenant_id") final String tenantId,
                                         @PathParam("platform") final String platformStr,
                                         @PathParam("channel_group_id") final String channelGroupId) {
        final Tenant tenant = getTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId().getValue(), authorization).ensurePermission("api.tenants.read");
        final Platform platform = getDtoPlatform(platformStr);
        final ChannelGroup channelGroup = getDtoChannelGroup(tenant, platform, channelGroupId);
        if(platform != Platform.TWITCH)
            throw new NotFoundException();
        final JoinedTwitchChannel joinedTwitchChannel = daoJoinedTwitchChannel.get(channelGroup.getId());
        if(joinedTwitchChannel == null)
            return new ApiPrimitive<>("PARTED");
        else
            return new ApiPrimitive<>("JOINED");
    }

    @Path("/{platform}/{channel_group_id}/state")
    @PUT
    public ApiPrimitive<String> putState(@HeaderParam("Authorization") final String authorization,
                                         @PathParam("tenant_id") final String tenantId,
                                         @PathParam("platform") final String platformStr,
                                         @PathParam("channel_group_id") final String channelGroupId,
                                         final ApiPrimitive<String> newState) {
        final Tenant tenant = getTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId().getValue(), authorization).ensurePermission("api.tenants.write");
        final Platform platform = getDtoPlatform(platformStr);
        final ChannelGroup channelGroup = getDtoChannelGroup(tenant, platform, channelGroupId);
        if(platform != Platform.TWITCH)
            throw new NotFoundException();
        if(newState.getValue().equals("JOINED"))
            daoJoinedTwitchChannel.join(channelGroup.getId());
        else if(newState.getValue().equals("PARTED"))
            daoJoinedTwitchChannel.delete(channelGroup.getId());
        else
            throw new ClientErrorException(Response.status(422).build());
        final JoinedTwitchChannel joinedTwitchChannel = daoJoinedTwitchChannel.get(channelGroup.getId());
        if(joinedTwitchChannel == null)
            return new ApiPrimitive<>("PARTED");
        else
            return new ApiPrimitive<>("JOINED");
    }

    @Path("/{platform}/{channel_group_id}/{channel_id}/message")
    @POST
    public Response postMessage(@HeaderParam("Authorization") final String authorization,
                                @PathParam("tenant_id") final String tenantId,
                                @PathParam("platform") final String platformStr,
                                @PathParam("channel_group_id") final String channelGroupId,
                                @PathParam("channel_id") final String channelId,
                                final ApiPrimitive<String> message) {
        final Tenant tenant = getTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId().getValue(), authorization).ensurePermission("api.tenants.message");
        final Platform platform = getDtoPlatform(platformStr);
        final Optional<tv.v1x1.common.dto.core.Channel> coreChannel = tenant.getChannel(platform, channelGroupId, channelId);
        if(!coreChannel.isPresent())
            throw new NotFoundException();
        Chat.message(module, coreChannel.get(), message.getValue());
        return Response.ok().build();
    }

    private Platform getDtoPlatform(final String platformStr) {
        try {
            return Platform.valueOf(platformStr.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new NotFoundException("Platform is invalid");
        }
    }

    private Tenant getTenant(final String tenantId) {
        final tv.v1x1.common.dto.db.Tenant dbTenant = daoTenant.getById(UUID.fromString(tenantId));
        if(dbTenant == null)
            throw new NotFoundException();
        return dbTenant.toCore(daoTenant);
    }

    private ChannelGroup getDtoChannelGroup(final Tenant tenant, final Platform platform, final String channelGroupId) {
        final Optional<tv.v1x1.common.dto.core.ChannelGroup> channelGroup = tenant.getChannelGroup(platform, channelGroupId);
        if(!channelGroup.isPresent())
            throw new NotFoundException();
        return new ChannelGroup(channelGroup.get());
    }

    private Channel getDtoChannel(final Tenant tenant, final Platform platform, final String channelGroupId, final String channelId) {
        final Optional<tv.v1x1.common.dto.core.Channel> channel = tenant.getChannel(platform, channelGroupId, channelId);
        if(!channel.isPresent())
            throw new NotFoundException();
        return new Channel(channel.get());
    }
}
