package tv.v1x1.modules.core.api.resources.tenant;

import com.google.inject.Inject;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dao.DAOTenantGroup;
import tv.v1x1.common.dto.db.ChannelGroup;
import tv.v1x1.common.dto.db.ChannelGroupPlatformMapping;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.dto.db.TenantGroup;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.modules.core.api.api.rest.ApiList;
import tv.v1x1.modules.core.api.api.rest.ApiPrimitive;
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
@Path("/api/v1/tenants/{tenant_id}/channels/{platform}/{channel_group_id}/mappings")
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
    public ApiList<tv.v1x1.modules.core.api.api.rest.ChannelGroupPlatformMapping> listMappings(@HeaderParam("Authorization") final String authorization,
                                                                                               @PathParam("tenant_id") final String tenantId,
                                                                                               @PathParam("platform") final String platformString,
                                                                                               @PathParam("channel_group_id") final String channelGroupId) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.read");
        final Platform platform = getDtoPlatform(platformString);
        final ChannelGroup channelGroup = getDtoChannelGroup(tenant, platform, channelGroupId);
        final Iterable<ChannelGroupPlatformMapping> channelGroupPlatformMappings = daoTenantGroup.getChannelGroupPlatformMappings(platform, channelGroup.getId());
        return new ApiList<>(StreamSupport.stream(channelGroupPlatformMappings.spliterator(), false)
                .map(cpm -> new tv.v1x1.modules.core.api.api.rest.ChannelGroupPlatformMapping(cpm.getPlatformGroup(), cpm.getGroupId().toString()))
                .collect(Collectors.toList()));
    }

    @Path("/{platform_group}")
    @PUT
    public ApiPrimitive<String> setMapping(@HeaderParam("Authorization") final String authorization,
                                      @PathParam("tenant_id") final String tenantId,
                                      @PathParam("platform") final String platformString,
                                      @PathParam("channel_group_id") final String channelGroupId,
                                      @PathParam("platform_group") final String platformGroup,
                                      final ApiPrimitive<String> groupId) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.write");
        final Platform platform = getDtoPlatform(platformString);
        final ChannelGroup channelGroup = getDtoChannelGroup(tenant, platform, channelGroupId);
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(tenant.toCore(daoTenant), UUID.fromString(groupId.getValue()));
        if(tenantGroup == null)
            throw new BadRequestException();
        daoTenantGroup.setChannelGroupPlatformMapping(platform, channelGroup.getId(), platformGroup, tenantGroup);
        return new ApiPrimitive<>(tenantGroup.getGroupId().toString());
    }

    @Path("/{platform_group}")
    @DELETE
    public Response removeMapping(@HeaderParam("Authorization") final String authorization,
                                  @PathParam("tenant_id") final String tenantId,
                                  @PathParam("platform") final String platformString,
                                  @PathParam("channel_group_id") final String channelGroupId,
                                  @PathParam("platform_group") final String platformGroup) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.write");
        final Platform platform = getDtoPlatform(platformString);
        final ChannelGroup channelGroup = getDtoChannelGroup(tenant, platform, channelGroupId);
        daoTenantGroup.clearChannelGroupPlatformMapping(platform, channelGroup.getId(), platformGroup);
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

    private ChannelGroup getDtoChannelGroup(final Tenant tenant, final Platform platform, final String channelGroupId) {
        final ChannelGroup channelGroup = daoTenant.getChannelGroup(platform, channelGroupId);
        if(channelGroup == null || !channelGroup.getTenantId().equals(tenant.getId()))
            throw new NotFoundException("Channel Group ID is invalid");
        return channelGroup;
    }
}
