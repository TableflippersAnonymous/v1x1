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
import tv.v1x1.common.dto.db.ChannelPlatformMapping;
import tv.v1x1.common.dto.db.Permission;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.TenantGroup;
import tv.v1x1.common.dto.db.TenantGroupMembership;
import tv.v1x1.common.dto.db.TenantGroupsByUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/16/2016.
 */
@Singleton
public class DAOTenantGroup {
    public static final Tenant GLOBAL_TENANT = new Tenant(new tv.v1x1.common.dto.core.UUID(UUID.nameUUIDFromBytes("__GLOBAL_TENANT__".getBytes())), ImmutableList.of());

    private final Session session;
    private final Mapper<TenantGroup> tenantGroupMapper;
    private final Mapper<TenantGroupMembership> tenantGroupMembershipMapper;
    private final Mapper<TenantGroupsByUser> tenantGroupsByUserMapper;
    private final Mapper<ChannelPlatformMapping> channelPlatformMappingMapper;
    private final TenantGroupAccessor accessor;

    @Accessor
    public interface TenantGroupAccessor {
        @Query("SELECT * FROM tenant_group WHERE tenant_id = ?")
        Result<TenantGroup> getAllGroupsByTenant(UUID tenantId);

        @Query("SELECT * FROM tenant_group_membership WHERE tenant_id = ? AND group_id = ?")
        Result<TenantGroupMembership> getUsersByGroup(UUID tenantId, UUID groupId);

        @Query("SELECT * FROM tenant_groups_by_user WHERE user_id = ?")
        Result<TenantGroupsByUser> getTenantsByUser(UUID userId);

        @Query("SELECT * FROM channel_platform_mapping WHERE platform = ? AND channel_id = ?")
        Result<ChannelPlatformMapping> getChannelPlatformMappings(Platform platform, String channelId);
    }

    @Inject
    public DAOTenantGroup(final MappingManager mappingManager) {
        session = mappingManager.getSession();
        tenantGroupMapper = mappingManager.mapper(TenantGroup.class);
        tenantGroupMembershipMapper = mappingManager.mapper(TenantGroupMembership.class);
        tenantGroupsByUserMapper = mappingManager.mapper(TenantGroupsByUser.class);
        channelPlatformMappingMapper = mappingManager.mapper(ChannelPlatformMapping.class);
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

    public ChannelPlatformMapping getChannelPlatformMapping(final Platform platform, final String channelId, final String platformGroup) {
        return channelPlatformMappingMapper.get(platform, channelId, platformGroup);
    }

    public Iterable<ChannelPlatformMapping> getChannelPlatformMappings(final Platform platform, final String channelId) {
        return accessor.getChannelPlatformMappings(platform, channelId);
    }

    public Set<UUID> getGroupsByPlatformGroups(final Platform platform, final String channelId, final Set<String> platformGroups) {
        return platformGroups.stream().map(platformGroup -> getChannelPlatformMapping(platform, channelId, platformGroup)).filter(mapping -> mapping != null).map(ChannelPlatformMapping::getGroupId).collect(Collectors.toSet());
    }

    public Set<Permission> getPermissionsFromGroups(final Tenant tenant, final Set<UUID> groupIds) {
        return groupIds.stream().map(groupId -> getTenantGroup(tenant, groupId)).filter(group -> group != null).map(TenantGroup::getPermissions).collect(HashSet::new, HashSet::addAll, HashSet::addAll);
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

    public Set<Permission> getChannelPlatformPermissions(final Tenant tenant, final Platform platform, final String channelId, final Set<String> platformGroups) {
        return getPermissionsFromGroups(tenant, getGroupsByPlatformGroups(platform, channelId, platformGroups));
    }

    public Set<Permission> getChannelPlatformPermissions(final Tenant tenant, final Platform platform, final String channelId, final String platformGroup) {
        return getChannelPlatformPermissions(tenant, platform, channelId, ImmutableSet.of(platformGroup));
    }

    public Set<Permission> getAllPermissions(final Tenant tenant, final GlobalUser globalUser) {
        final Set<Permission> set = new HashSet<>();
        if(tenant != null)
            set.addAll(getTenantPermissions(tenant, globalUser));
        set.addAll(getGlobalPermissions(globalUser));
        return set;
    }

    public Set<Permission> getAllPermissions(final Tenant tenant, final GlobalUser globalUser, final Platform platform, final String channelId, final Set<String> platformGroups) {
        final Set<Permission> set = getAllPermissions(tenant, globalUser);
        if(tenant != null)
            set.addAll(getChannelPlatformPermissions(tenant, platform, channelId, platformGroups));
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

    public void setChannelPlatformMapping(final Platform platform, final String channelId, final String platformGroup, final TenantGroup tenantGroup) {
        channelPlatformMappingMapper.save(new ChannelPlatformMapping(platform, channelId, platformGroup, tenantGroup.getGroupId()));
    }

    public void clearChannelPlatformMapping(final Platform platform, final String channelId, final String platformGroup) {
        channelPlatformMappingMapper.delete(new ChannelPlatformMapping(platform, channelId, platformGroup, null));
    }
}
