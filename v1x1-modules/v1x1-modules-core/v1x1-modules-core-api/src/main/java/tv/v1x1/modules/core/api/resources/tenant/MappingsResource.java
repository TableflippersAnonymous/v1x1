package tv.v1x1.modules.core.api.resources.tenant;

import com.google.inject.Inject;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dao.DAOTenantGroup;
import tv.v1x1.common.dto.db.ChannelPlatformMapping;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.dto.db.TenantGroup;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.modules.core.api.api.rest.ApiList;
import tv.v1x1.modules.core.api.api.rest.ApiPrimitive;
import tv.v1x1.modules.core.api.api.rest.Channel;
import tv.v1x1.modules.core.api.auth.Authorizer;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by cobi on 10/26/2016.
 */
/*
  /tenants/{tenant_id}/channels/{platform}/{channel_id}
   /mappings - GET: List of mappings
    /{platform_group} - PUT: Set group; DELETE: Unset group
 */
@Path("/api/v1/tenants/{tenant_id}/channels/{platform}/{channel_id}/mappings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MappingsResource {
    private final Authorizer authorizer;
    private final DAOTenantGroup daoTenantGroup;
    private final DAOTenant daoTenant;

    @Inject
    public MappingsResource(final Authorizer authorizer, final DAOManager daoManager) {
        this.authorizer = authorizer;
        this.daoTenantGroup = daoManager.getDaoTenantGroup();
        this.daoTenant = daoManager.getDaoTenant();
    }

    @GET
    public ApiList<tv.v1x1.modules.core.api.api.rest.ChannelPlatformMapping> listMappings(@HeaderParam("Authorization") final String authorization,
                                                                                          @PathParam("tenant_id") final String tenantId,
                                                                                          @PathParam("platform") final String platformString,
                                                                                          @PathParam("channel_id") final String channelId) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.read");
        final Platform platform = getDtoPlatform(platformString);
        final Channel channel = getDtoChannel(tenant, platform, channelId);
        final Iterable<ChannelPlatformMapping> channelPlatformMappings = daoTenantGroup.getChannelPlatformMappings(platform, channel.getChannelId());
        return new ApiList<>(StreamSupport.stream(channelPlatformMappings.spliterator(), false)
                .map(cpm -> new tv.v1x1.modules.core.api.api.rest.ChannelPlatformMapping(cpm.getPlatformGroup(), cpm.getGroupId().toString()))
                .collect(Collectors.toList()));
    }

    @Path("/{platform_group}")
    @PUT
    public ApiPrimitive<String> setMapping(@HeaderParam("Authorization") final String authorization,
                                      @PathParam("tenant_id") final String tenantId,
                                      @PathParam("platform") final String platformString,
                                      @PathParam("channel_id") final String channelId,
                                      @PathParam("platform_group") final String platformGroup,
                                      final ApiPrimitive<String> groupId) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.write");
        final Platform platform = getDtoPlatform(platformString);
        final Channel channel = getDtoChannel(tenant, platform, channelId);
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(tenant.toCore(), UUID.fromString(groupId.getValue()));
        if(tenantGroup == null)
            throw new BadRequestException();
        daoTenantGroup.setChannelPlatformMapping(platform, channel.getChannelId(), platformGroup, tenantGroup);
        return new ApiPrimitive<>(tenantGroup.getGroupId().toString());
    }

    @Path("/{platform_group}")
    @DELETE
    public Response removeMapping(@HeaderParam("Authorization") final String authorization,
                                  @PathParam("tenant_id") final String tenantId,
                                  @PathParam("platform") final String platformString,
                                  @PathParam("channel_id") final String channelId,
                                  @PathParam("platform_group") final String platformGroup) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.write");
        final Platform platform = getDtoPlatform(platformString);
        final Channel channel = getDtoChannel(tenant, platform, channelId);
        daoTenantGroup.clearChannelPlatformMapping(platform, channel.getChannelId(), platformGroup);
        return Response.noContent().build();
    }

    private Tenant getDtoTenant(final String tenantIdStr) {
        final UUID tenantId;
        try {
            tenantId = UUID.fromString(tenantIdStr);
        } catch (final IllegalArgumentException e) {
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
        } catch(final IllegalArgumentException e) {
            throw new NotFoundException("Platform is invalid");
        }
    }

    private Channel getDtoChannel(final Tenant tenant, final Platform platform, final String channelId) {
        final Tenant.Entry channelEntry = tenant.getEntries().stream().filter(entry -> entry.getPlatform().equals(platform))
                .filter(entry -> entry.getChannelId().equals(channelId)).findFirst()
                .orElseThrow(() -> new NotFoundException("Channel not found"));
        return new Channel(tenant.getId(), channelEntry.getPlatform(), channelEntry.getDisplayName(), channelEntry.getChannelId());
    }
}
