package tv.v1x1.modules.core.api.resources.management;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dao.DAOTenantGroup;
import tv.v1x1.common.dto.db.GlobalUser;
import tv.v1x1.common.dto.db.Permission;
import tv.v1x1.common.dto.db.TenantGroup;
import tv.v1x1.common.services.persistence.DAOManager;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by cobi on 10/26/2016.
 */
/*
  /management
    /groups - GET: List all global groups; POST: Create group
      /{group} - GET: return group data; DELETE: Remove group
        /users - GET: list users in group; POST: grant group to user
          /{user} - DELETE: Remove user from group
        /permissions - GET: List permissions in group; POST: grant permission to group
          /{permission} - DELETE: revoke permission from group
 */
@Path("/api/v1/management/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupsResource {
    private final Authorizer authorizer;
    private final DAOTenantGroup daoTenantGroup;
    private final DAOGlobalUser daoGlobalUser;

    @Inject
    public GroupsResource(final Authorizer authorizer, final DAOManager daoManager) {
        this.authorizer = authorizer;
        this.daoTenantGroup = daoManager.getDaoTenantGroup();
        this.daoGlobalUser = daoManager.getDaoGlobalUser();
    }

    @GET
    public List<String> listGroups(@HeaderParam("Authorization") final String authorization) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_permissions.read");
        return Lists.newArrayList(StreamSupport.stream(
                daoTenantGroup.getAllGroupsByTenant(DAOTenantGroup.GLOBAL_TENANT).spliterator(), false)
                .map(tenantGroup -> tenantGroup.getGroupId().toString())
                .collect(Collectors.toList()));
    }

    @POST
    public Group createGroup(@HeaderParam("Authorization") final String authorization,
                             final String name) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_permissions.write");
        final TenantGroup tenantGroup = daoTenantGroup.createGroup(DAOTenantGroup.GLOBAL_TENANT, name);
        return new Group(tenantGroup);
    }

    @Path("/{group}")
    @GET
    public Group getGroup(@HeaderParam("Authorization") final String authorization,
                          @PathParam("group") final String groupId) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_permissions.read");
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(DAOTenantGroup.GLOBAL_TENANT, UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        return new Group(tenantGroup);
    }

    @Path("/{group}")
    @DELETE
    public Response deleteGroup(@HeaderParam("Authorization") final String authorization,
                                @PathParam("group") final String groupId) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_permissions.write");
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(DAOTenantGroup.GLOBAL_TENANT, UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        daoTenantGroup.deleteGroup(tenantGroup);
        return Response.noContent().build();
    }

    @Path("/{group}/users")
    @GET
    public List<String> getUsersInGroup(@HeaderParam("Authorization") final String authorization,
                                        @PathParam("group") final String groupId) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_permissions.read");
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(DAOTenantGroup.GLOBAL_TENANT, UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        return Lists.newArrayList(Iterables.transform(daoTenantGroup.getUsersByGroup(tenantGroup), UUID::toString));
    }

    @Path("/{group}/users")
    @POST
    public List<String> addUserToGroup(@HeaderParam("Authorization") final String authorization,
                                       @PathParam("group") final String groupId, final String userId) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_permissions.write");
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(userId));
        if(globalUser == null)
            throw new NotFoundException();
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(DAOTenantGroup.GLOBAL_TENANT, UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        daoTenantGroup.addUserToGroup(tenantGroup, globalUser.toCore());
        return daoTenantGroup.getGroupsByUser(DAOTenantGroup.GLOBAL_TENANT, globalUser.toCore()).getGroups().stream().map(UUID::toString).collect(Collectors.toList());
    }

    @Path("/{group}/users/{user}")
    @DELETE
    public Response removeUserFromGroup(@HeaderParam("Authorization") final String authorization,
                                        @PathParam("group") final String groupId,
                                        @PathParam("user") final String userId) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_permissions.write");
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(userId));
        if(globalUser == null)
            throw new NotFoundException();
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(DAOTenantGroup.GLOBAL_TENANT, UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        daoTenantGroup.addUserToGroup(tenantGroup, globalUser.toCore());
        return Response.noContent().build();
    }

    @Path("/{group}/permissions")
    @GET
    public List<String> listPermissionsForGroup(@HeaderParam("Authorization") final String authorization,
                                                @PathParam("group") final String groupId) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_permissions.read");
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(DAOTenantGroup.GLOBAL_TENANT, UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        return tenantGroup.getPermissions().stream().map(Permission::getNode).collect(Collectors.toList());
    }

    @Path("/{group}/permissions")
    @POST
    public List<String> grantPermissionToGroup(@HeaderParam("Authorization") final String authorization,
                                               @PathParam("group") final String groupId, final String permissionNode) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_permissions.write");
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(DAOTenantGroup.GLOBAL_TENANT, UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        daoTenantGroup.addPermissionsToGroup(tenantGroup, ImmutableSet.of(new Permission(permissionNode)));
        return daoTenantGroup.getTenantGroup(DAOTenantGroup.GLOBAL_TENANT, UUID.fromString(groupId)).getPermissions().stream().map(Permission::getNode).collect(Collectors.toList());
    }

    @Path("/{group}/permissions/{permission}")
    @DELETE
    public Response revokePermissionFromGroup(@HeaderParam("Authorization") final String authorization,
                                              @PathParam("group") final String groupId,
                                              @PathParam("permission") final String permissionNode) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_permissions.write");
        final TenantGroup tenantGroup = daoTenantGroup.getTenantGroup(DAOTenantGroup.GLOBAL_TENANT, UUID.fromString(groupId));
        if(tenantGroup == null)
            throw new NotFoundException();
        daoTenantGroup.removePermissionsFromGroup(tenantGroup, ImmutableSet.of(new Permission(permissionNode)));
        return Response.noContent().build();
    }
}
