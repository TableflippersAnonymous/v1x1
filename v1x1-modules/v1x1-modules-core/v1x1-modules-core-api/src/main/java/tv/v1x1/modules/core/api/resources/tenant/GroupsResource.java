package tv.v1x1.modules.core.api.resources.tenant;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dao.DAOTenantGroup;
import tv.v1x1.common.dto.db.GlobalUser;
import tv.v1x1.common.dto.db.Permission;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.dto.db.TenantGroup;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.modules.core.api.api.ApiList;
import tv.v1x1.modules.core.api.api.ApiPrimitive;
import tv.v1x1.modules.core.api.api.Group;
import tv.v1x1.modules.core.api.auth.Authorizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by cobi on 2/1/2017.
 */
/*
  /tenants/{tenant}/
    /groups - GET: List all tenant groups; POST: Create group
      /{group} - GET: return group data; DELETE: Remove group
        /users - GET: list users in group; POST: grant group to user
          /{user} - DELETE: Remove user from group
        /permissions - GET: List permissions in group; POST: grant permission to group
          /{permission} - DELETE: revoke permission from group
 */
@Path("/api/v1/tenants/{tenant}/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupsResource {
    private final Authorizer authorizer;
    private final DAOTenantGroup daoTenantGroup;
    private final DAOGlobalUser daoGlobalUser;
    private final DAOTenant daoTenant;

    @Inject
    public GroupsResource(final Authorizer authorizer, final DAOManager daoManager) {
        this.authorizer = authorizer;
        this.daoTenantGroup = daoManager.getDaoTenantGroup();
        this.daoGlobalUser = daoManager.getDaoGlobalUser();
        this.daoTenant = daoManager.getDaoTenant();
    }

    @GET
    public ApiList<String> listGroups(@HeaderParam("Authorization") final String authorization,
                                      @PathParam("tenant") final String tenantId) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.read");
        return new ApiList<>(StreamSupport.stream(
                daoTenantGroup.getAllGroupsByTenant(tenant.toCore()).spliterator(), false)
                .map(tenantGroup -> tenantGroup.getGroupId().toString())
                .collect(Collectors.toList()));
    }

    @POST
    public Group createGroup(@HeaderParam("Authorization") final String authorization,
                             @PathParam("tenant") final String tenantId,
                             final ApiPrimitive<String> name) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.write");
        final TenantGroup tenantGroup = daoTenantGroup.createGroup(tenant.toCore(), name.getValue());
        return new Group(tenantGroup);
    }

    @Path("/{group}")
    @GET
    public Group getGroup(@HeaderParam("Authorization") final String authorization,
                          @PathParam("tenant") final String tenantId,
                          @PathParam("group") final String groupId) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.read");
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(tenant.toCore(), UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        return new Group(tenantGroup);
    }

    @Path("/{group}")
    @DELETE
    public Response deleteGroup(@HeaderParam("Authorization") final String authorization,
                                @PathParam("tenant") final String tenantId,
                                @PathParam("group") final String groupId) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.write");
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(tenant.toCore(), UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        daoTenantGroup.deleteGroup(tenantGroup);
        return Response.noContent().build();
    }

    @Path("/{group}/users")
    @GET
    public ApiList<String> getUsersInGroup(@HeaderParam("Authorization") final String authorization,
                                           @PathParam("tenant") final String tenantId,
                                           @PathParam("group") final String groupId) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.read");
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(tenant.toCore(), UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        return new ApiList<>(Lists.newArrayList(Iterables.transform(daoTenantGroup.getUsersByGroup(tenantGroup), UUID::toString)));
    }

    @Path("/{group}/users")
    @POST
    public ApiList<String> addUserToGroup(@HeaderParam("Authorization") final String authorization,
                                          @PathParam("tenant") final String tenantId,
                                          @PathParam("group") final String groupId, final ApiPrimitive<String> userId) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.write");
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(userId.getValue()));
        if(globalUser == null)
            throw new NotFoundException();
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(tenant.toCore(), UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        daoTenantGroup.addUserToGroup(tenantGroup, globalUser.toCore());
        return new ApiList<>(daoTenantGroup.getGroupsByUser(tenant.toCore(), globalUser.toCore()).getGroups().stream().map(UUID::toString).collect(Collectors.toList()));
    }

    @Path("/{group}/users/{user}")
    @DELETE
    public Response removeUserFromGroup(@HeaderParam("Authorization") final String authorization,
                                        @PathParam("tenant") final String tenantId,
                                        @PathParam("group") final String groupId,
                                        @PathParam("user") final String userId) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.write");
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(userId));
        if(globalUser == null)
            throw new NotFoundException();
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(tenant.toCore(), UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        daoTenantGroup.removeUserFromGroup(tenantGroup, globalUser.toCore());
        return Response.noContent().build();
    }

    @Path("/{group}/permissions")
    @GET
    public ApiList<String> listPermissionsForGroup(@HeaderParam("Authorization") final String authorization,
                                                   @PathParam("tenant") final String tenantId,
                                                   @PathParam("group") final String groupId) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.read");
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(tenant.toCore(), UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        return new ApiList<>(tenantGroup.getPermissions().stream().map(Permission::getNode).collect(Collectors.toList()));
    }

    @Path("/{group}/permissions")
    @POST
    public ApiList<String> grantPermissionToGroup(@HeaderParam("Authorization") final String authorization,
                                                  @PathParam("tenant") final String tenantId,
                                                  @PathParam("group") final String groupId,
                                                  final ApiPrimitive<String> permissionNode) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.write");
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(tenant.toCore(), UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        daoTenantGroup.addPermissionsToGroup(tenantGroup, ImmutableSet.of(new Permission(permissionNode.getValue())));
        return new ApiList<>(daoTenantGroup.getTenantGroup(tenant.toCore(), UUID.fromString(groupId))
                .getPermissions().stream().map(Permission::getNode).collect(Collectors.toList()));
    }

    @Path("/{group}/permissions/{permission}")
    @DELETE
    public Response revokePermissionFromGroup(@HeaderParam("Authorization") final String authorization,
                                              @PathParam("tenant") final String tenantId,
                                              @PathParam("group") final String groupId,
                                              @PathParam("permission") final String permissionNode) {
        final Tenant tenant = getDtoTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId(), authorization).ensurePermission("api.permissions.write");
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(tenant.toCore(), UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        daoTenantGroup.removePermissionsFromGroup(tenantGroup, ImmutableSet.of(new Permission(permissionNode)));
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
}
