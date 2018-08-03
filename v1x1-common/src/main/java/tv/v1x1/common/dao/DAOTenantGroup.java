package tv.v1x1.common.dao;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.db.ChannelGroupPlatformMapping;
import tv.v1x1.common.dto.db.Permission;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.TenantGroup;
import tv.v1x1.common.dto.db.TenantGroupMembership;
import tv.v1x1.common.dto.db.TenantGroupsByUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Groups of users within a tenant
 * Created by naomi on 10/16/2016.
 */
@Singleton
public class DAOTenantGroup {
    public static final Tenant GLOBAL_TENANT = new Tenant(new tv.v1x1.common.dto.core.UUID(UUID.nameUUIDFromBytes("__GLOBAL_TENANT__".getBytes())), "Global Tenant", ImmutableList.of());

    private final Session session;
    private final Mapper<TenantGroup> tenantGroupMapper;
    private final Mapper<TenantGroupMembership> tenantGroupMembershipMapper;
    private final Mapper<TenantGroupsByUser> tenantGroupsByUserMapper;
    private final Mapper<ChannelGroupPlatformMapping> channelGroupPlatformMappingMapper;
    private final TenantGroupAccessor accessor;

    @Accessor
    public interface TenantGroupAccessor {
        @Query("SELECT * FROM tenant_group WHERE tenant_id = ?")
        Result<TenantGroup> getAllGroupsByTenant(UUID tenantId);

        @Query("SELECT * FROM tenant_group_membership WHERE tenant_id = ? AND group_id = ?")
        Result<TenantGroupMembership> getUsersByGroup(UUID tenantId, UUID groupId);

        @Query("SELECT * FROM tenant_groups_by_user WHERE user_id = ?")
        Result<TenantGroupsByUser> getTenantsByUser(UUID userId);

        @Query("SELECT * FROM channel_group_platform_mapping WHERE platform = ? AND channel_group_id = ?")
        Result<ChannelGroupPlatformMapping> getChannelGroupPlatformMappings(Platform platform, String channelGroupId);
    }

    @Inject
    public DAOTenantGroup(final MappingManager mappingManager) {
        session = mappingManager.getSession();
        tenantGroupMapper = mappingManager.mapper(TenantGroup.class);
        tenantGroupMembershipMapper = mappingManager.mapper(TenantGroupMembership.class);
        tenantGroupsByUserMapper = mappingManager.mapper(TenantGroupsByUser.class);
        channelGroupPlatformMappingMapper = mappingManager.mapper(ChannelGroupPlatformMapping.class);
        accessor = mappingManager.createAccessor(TenantGroupAccessor.class);
    }

    public TenantGroupsByUser getGroupsByUser(final UUID tenantId, final UUID userId) {
        final TenantGroupsByUser tenantGroupsByUser = tenantGroupsByUserMapper.get(userId, tenantId);
        if(tenantGroupsByUser == null)
            return new TenantGroupsByUser(userId, tenantId, new ArrayList<>());
        return tenantGroupsByUser;
    }

    public TenantGroupsByUser getGroupsByUser(final Tenant tenant, final GlobalUser globalUser) {
        return getGroupsByUser(tenant.getId().getValue(), globalUser.getId().getValue());
    }

    public TenantGroup getTenantGroup(final Tenant tenant, final UUID groupId) {
        return tenantGroupMapper.get(tenant.getId().getValue(), groupId);
    }

    public ChannelGroupPlatformMapping getChannelGroupPlatformMapping(final Platform platform, final String channelGroupId, final String platformGroup) {
        return channelGroupPlatformMappingMapper.get(platform, channelGroupId, platformGroup);
    }

    public Iterable<ChannelGroupPlatformMapping> getChannelGroupPlatformMappings(final Platform platform, final String channelGroupId) {
        return accessor.getChannelGroupPlatformMappings(platform, channelGroupId);
    }

    public Set<UUID> getGroupsByPlatformGroups(final Platform platform, final String channelGroupId, final Set<String> platformGroups) {
        return platformGroups.stream().map(platformGroup -> getChannelGroupPlatformMapping(platform, channelGroupId, platformGroup)).filter(Objects::nonNull).map(ChannelGroupPlatformMapping::getGroupId).collect(Collectors.toSet());
    }

    public Set<Permission> getPermissionsFromGroups(final Tenant tenant, final Set<UUID> groupIds) {
        return groupIds.stream().map(groupId -> getTenantGroup(tenant, groupId)).filter(Objects::nonNull).map(TenantGroup::getPermissions).collect(HashSet::new, HashSet::addAll, HashSet::addAll);
    }

    public Set<Permission> getTenantPermissions(final Tenant tenant, final GlobalUser globalUser) {
        final TenantGroupsByUser tenantGroupsByUser = getGroupsByUser(tenant, globalUser);
        if(tenantGroupsByUser == null)
            return ImmutableSet.of();
        return getPermissionsFromGroups(tenant, new HashSet<>(tenantGroupsByUser.getGroups()));
    }

    public Set<Permission> getGlobalPermissions(final GlobalUser globalUser) {
        return getTenantPermissions(GLOBAL_TENANT, globalUser);
    }

    public Set<Permission> getChannelGroupPlatformPermissions(final Tenant tenant, final Platform platform, final String channelGroupId, final Set<String> platformGroups) {
        return getPermissionsFromGroups(tenant, getGroupsByPlatformGroups(platform, channelGroupId, platformGroups));
    }

    public Set<Permission> getChannelGroupPlatformPermissions(final Tenant tenant, final Platform platform, final String channelGroupId, final String platformGroup) {
        return getChannelGroupPlatformPermissions(tenant, platform, channelGroupId, ImmutableSet.of(platformGroup));
    }

    public Set<Permission> getAllPermissions(final Tenant tenant, final GlobalUser globalUser) {
        final Set<Permission> set = new HashSet<>();
        if(tenant != null)
            set.addAll(getTenantPermissions(tenant, globalUser));
        set.addAll(getGlobalPermissions(globalUser));
        return set;
    }

    public Set<Permission> getAllPermissions(final Tenant tenant, final GlobalUser globalUser, final Platform platform, final String channelGroupId, final Set<String> platformGroups) {
        final Set<Permission> set = getAllPermissions(tenant, globalUser);
        if(tenant != null)
            set.addAll(getChannelGroupPlatformPermissions(tenant, platform, channelGroupId, platformGroups));
        return set;
    }

    public Iterable<TenantGroup> getAllGroupsByTenant(final Tenant tenant) {
        return accessor.getAllGroupsByTenant(tenant.getId().getValue());
    }

    public Iterable<UUID> getUsersByGroup(final UUID tenantId, final UUID groupId) {
        return Iterables.transform(accessor.getUsersByGroup(tenantId, groupId), TenantGroupMembership::getUserId);
    }

    public Iterable<UUID> getUsersByGroup(final Tenant tenant, final UUID groupId) {
        return getUsersByGroup(tenant.getId().getValue(), groupId);
    }

    public Iterable<UUID> getUsersByGroup(final TenantGroup tenantGroup) {
        return getUsersByGroup(tenantGroup.getTenantId(), tenantGroup.getGroupId());
    }

    public Iterable<UUID> getTenantsByUser(final UUID userId) {
        return Iterables.transform(accessor.getTenantsByUser(userId), TenantGroupsByUser::getTenantId);
    }

    public Iterable<UUID> getTenantsByUser(final GlobalUser globalUser) {
        return getTenantsByUser(globalUser.getId().getValue());
    }

    public TenantGroup createGroup(final Tenant tenant, final String name) {
        final TenantGroup tenantGroup = new TenantGroup(tenant.getId().getValue(), UUID.randomUUID(), name, new ArrayList<>());
        tenantGroupMapper.save(tenantGroup);
        return tenantGroup;
    }

    public TenantGroup addUserToGroup(final TenantGroup tenantGroup, final GlobalUser globalUser) {
        final TenantGroupMembership tenantGroupMembership = new TenantGroupMembership(tenantGroup.getTenantId(), tenantGroup.getGroupId(), globalUser.getId().getValue());
        final TenantGroupsByUser tenantGroupsByUser = getGroupsByUser(tenantGroup.getTenantId(), globalUser.getId().getValue());
        final Set<UUID> groups = new HashSet<>(tenantGroupsByUser.getGroups());
        groups.add(tenantGroup.getGroupId());
        tenantGroupsByUser.getGroups().clear();
        tenantGroupsByUser.getGroups().addAll(groups);
        final BatchStatement b = new BatchStatement();
        b.add(tenantGroupMembershipMapper.saveQuery(tenantGroupMembership));
        b.add(tenantGroupsByUserMapper.saveQuery(tenantGroupsByUser));
        session.execute(b);
        return tenantGroup;
    }

    public TenantGroup removeUserFromGroup(final TenantGroup tenantGroup, final UUID userId) {
        final TenantGroupMembership tenantGroupMembership = new TenantGroupMembership(tenantGroup.getTenantId(), tenantGroup.getGroupId(), userId);
        final TenantGroupsByUser tenantGroupsByUser = getGroupsByUser(tenantGroup.getTenantId(), userId);
        tenantGroupsByUser.getGroups().remove(tenantGroup.getGroupId());
        final BatchStatement b = new BatchStatement();
        b.add(tenantGroupMembershipMapper.deleteQuery(tenantGroupMembership.getTenantId(), tenantGroupMembership.getGroupId(), tenantGroupMembership.getUserId()));
        b.add(tenantGroupsByUserMapper.saveQuery(tenantGroupsByUser));
        session.execute(b);
        return tenantGroup;
    }

    public TenantGroup removeUserFromGroup(final TenantGroup tenantGroup, final GlobalUser globalUser) {
        return removeUserFromGroup(tenantGroup, globalUser.getId().getValue());
    }

    public TenantGroup addPermissionsToGroup(final TenantGroup tenantGroup, final Set<Permission> permissions) {
        tenantGroup.getPermissions().addAll(permissions);
        final Set<Permission> set = new LinkedHashSet<>(tenantGroup.getPermissions());
        tenantGroup.getPermissions().clear();
        tenantGroup.getPermissions().addAll(set);
        tenantGroupMapper.save(tenantGroup);
        return tenantGroup;
    }

    public TenantGroup removePermissionsFromGroup(final TenantGroup tenantGroup, final Set<Permission> permissions) {
        tenantGroup.getPermissions().removeAll(permissions);
        tenantGroupMapper.save(tenantGroup);
        return tenantGroup;
    }

    public void deleteGroup(final TenantGroup tenantGroup) {
        getUsersByGroup(tenantGroup).forEach(userId -> removeUserFromGroup(tenantGroup, userId));
        tenantGroupMapper.delete(tenantGroup);
    }

    public void setChannelGroupPlatformMapping(final Platform platform, final String channelGroupId, final String platformGroup, final TenantGroup tenantGroup) {
        channelGroupPlatformMappingMapper.save(new ChannelGroupPlatformMapping(platform, channelGroupId, platformGroup, tenantGroup.getGroupId()));
    }

    public void clearChannelGroupPlatformMapping(final Platform platform, final String channelGroupId, final String platformGroup) {
        channelGroupPlatformMappingMapper.delete(new ChannelGroupPlatformMapping(platform, channelGroupId, platformGroup, null));
    }
}
