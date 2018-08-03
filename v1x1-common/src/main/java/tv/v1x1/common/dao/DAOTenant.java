package tv.v1x1.common.dao;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.redisson.api.RedissonClient;
import tv.v1x1.common.dto.db.Channel;
import tv.v1x1.common.dto.db.ChannelGroup;
import tv.v1x1.common.dto.db.ChannelGroupsByTenant;
import tv.v1x1.common.dto.db.ChannelsByChannelGroup;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.services.persistence.Deduplicator;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.services.state.NoSuchTargetException;
import tv.v1x1.common.util.data.CompositeKey;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Sends/Retrieves Tenants to/from the database
 * @author Naomi
 */
@Singleton
public class DAOTenant {
    private final Deduplicator createDeduplicator;
    private final Session session;
    private final Mapper<Tenant> tenantMapper;
    private final Mapper<ChannelGroup> channelGroupMapper;
    private final Mapper<Channel> channelMapper;
    private final TenantAccessor tenantAccessor;
    private final DisplayNameService displayNameService;

    @Accessor
    public interface TenantAccessor {
        @Query("SELECT * FROM channels_by_channel_group WHERE platform = ? AND channel_group_id = ?")
        Result<ChannelsByChannelGroup> getChannelsByChannelGroup(Platform platform, String channelGroupId);

        @Query("SELECT * FROM channel_groups_by_tenant WHERE tenant_id = ?")
        Result<ChannelGroupsByTenant> getChannelGroupsByTenant(UUID tenantId);
    }

    @Inject
    public DAOTenant(final RedissonClient redissonClient, final MappingManager mappingManager, final DisplayNameService displayNameService) {
        this.session = mappingManager.getSession();
        this.tenantMapper = mappingManager.mapper(Tenant.class);
        this.channelGroupMapper = mappingManager.mapper(ChannelGroup.class);
        this.channelMapper = mappingManager.mapper(Channel.class);
        this.tenantAccessor = mappingManager.createAccessor(TenantAccessor.class);
        this.createDeduplicator = new Deduplicator(redissonClient, "Common|DAOTenant");
        this.displayNameService = displayNameService;
    }

    public Tenant getById(final UUID id) {
        return tenantMapper.get(id);
    }

    public Channel getChannel(final Platform platform, final String channelId) {
        return channelMapper.get(platform, channelId);
    }

    /**
     * Get a {@link tv.v1x1.common.dto.core.Channel} object
     * @param platform
     * @param channelId
     * @return a Channel
     * @throws NoSuchChannelException When a channel can't be found
     */
    public tv.v1x1.common.dto.core.Channel getChannelAsCore(final Platform platform, final String channelId) throws NoSuchChannelException {
        final Channel dbChannel = getChannel(platform, channelId);
        if(dbChannel != null)
            return dbChannel.toCore(this);
        throw new NoSuchChannelException("Channel does not exist in any Tenant");
    }

    /**
     *  Gets a {@link tv.v1x1.common.dto.core.Channel}, ensuring it comes from the same {@link Tenant} as {@code context}
     * @param context
     * @param channelId
     * @return
     * @throws NoSuchChannelException
     */
    public tv.v1x1.common.dto.core.Channel getChannelInTenant(final tv.v1x1.common.dto.core.Channel context, final String channelId) throws NoSuchChannelException {
        final tv.v1x1.common.dto.core.Channel channel = getChannelAsCore(context.getPlatform(), channelId);
        if(channel.getTenant().equals(context.getTenant()))
            return channel;
        throw new NoSuchChannelException("Channel is from a different Tenant");
    }

    public Tenant getByChannel(final Platform platform, final String channelId) {
        return getByChannel(getChannel(platform, channelId));
    }

    public Tenant getByChannel(final Channel channel) {
        if(channel == null)
            return null;
        return getByChannelGroup(getChannelGroup(channel));
    }

    public Tenant getByChannelGroup(final ChannelGroup channelGroup) {
        if(channelGroup == null)
            return null;
        return getById(channelGroup.getTenantId());
    }

    public ChannelGroup getChannelGroup(final Platform platform, final String channelGroupId) {
        return channelGroupMapper.get(platform, channelGroupId);
    }

    public ChannelGroup getChannelGroup(final Channel channel) {
        return getChannelGroup(channel.getPlatform(), channel.getChannelGroupId());
    }

    public Set<Channel> getChannelsByChannelGroup(final ChannelGroup channelGroup) {
        return ImmutableSet.copyOf(tenantAccessor.getChannelsByChannelGroup(channelGroup.getPlatform(), channelGroup.getId()).all()
                .stream().map(ChannelsByChannelGroup::toChannel).collect(Collectors.toList()));
    }

    public Set<ChannelGroup> getChannelGroups(final Tenant tenant) {
        return ImmutableSet.copyOf(tenantAccessor.getChannelGroupsByTenant(tenant.getId()).all()
                .stream().map(ChannelGroupsByTenant::toChannelGroup).collect(Collectors.toList()));
    }

    public Tenant getOrCreate(final Platform platform, final String channelGroupId, final String displayName) {
        final ChannelGroup channelGroup = getChannelGroup(platform, channelGroupId);
        if(channelGroup == null)
            return createTenant(platform, channelGroupId, null, displayName);
        final Tenant tenant = getByChannelGroup(channelGroup);
        if(tenant == null)
            throw new IllegalStateException("Tenant null but inverse_tenant for: " + channelGroupId + " " + platform);
        return tenant;
    }

    public tv.v1x1.common.dto.core.Tenant getAsCore(final Platform platform, final String channelGroupId) {
        return getOrCreate(platform, channelGroupId, null).toCore(this);
    }

    public Tenant getOrCreate(final Platform platform, final String channelGroupId, final String channelId, final String displayName) {
        final Channel channel = getChannel(platform, channelId);
        ChannelGroup channelGroup;
        if(channel == null) {
            channelGroup = getChannelGroup(platform, channelGroupId);
            if(channelGroup == null)
                return createTenant(platform, channelGroupId, channelId, displayName);
            else
                channelGroup = addChannel(channelGroup, channelId, displayName);
        } else
            channelGroup = getChannelGroup(channel);
        final Tenant tenant = getByChannelGroup(channelGroup);
        if(tenant == null)
            throw new IllegalStateException("Tenant null but inverse_tenant for: " + channelGroupId + " " + channelId + " " + platform);
        return tenant;
    }

    public Tenant createTenant(final Platform platform, final String channelGroupId, final String channelId, String displayName) {
        if(createDeduplicator.seenAndAdd(new tv.v1x1.common.dto.core.UUID(UUID.nameUUIDFromBytes(CompositeKey.makeKey(platform.name(), channelId == null ? channelGroupId : channelId))))) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(channelId == null)
                return getOrCreate(platform, channelGroupId, displayName);
            return getOrCreate(platform, channelGroupId, channelId, displayName);
        }
        if(displayName == null) {
            try {
                displayName = displayNameService.getChannelGroupDisplayNameFromId(platform, channelGroupId);
            } catch(NoSuchTargetException e) {
                throw new RuntimeException(e);
            }
        }
        final Tenant tenant = new Tenant(UUID.randomUUID(), displayName);
        final BatchStatement b = new BatchStatement();
        b.add(tenantMapper.saveQuery(tenant));
        b.add(channelGroupMapper.saveQuery(new ChannelGroup(platform, channelGroupId, displayName, tenant.getId())));
        if(channelId != null)
            b.add(channelMapper.saveQuery(new Channel(platform, channelId, displayName, channelGroupId)));
        session.execute(b);
        return tenant;
    }

    public ChannelGroup addChannel(final ChannelGroup channelGroup, final String channelId, final String displayName) {
        channelMapper.save(new Channel(channelGroup.getPlatform(), channelId, displayName, channelGroup.getId()));
        return channelGroup;
    }

    public ChannelGroup removeChannel(final ChannelGroup channelGroup, final String channelId) {
        channelMapper.delete(getChannel(channelGroup.getPlatform(), channelId));
        return channelGroup;
    }

    public Tenant addChannelGroup(final Tenant tenant, final Platform platform, final String channelGroupId, final String displayName) {
        channelGroupMapper.save(new ChannelGroup(platform, channelGroupId, displayName, tenant.getId()));
        return tenant;
    }

    public Tenant removeChannelGroup(final Tenant tenant, final Platform platform, final String channelGroupId) {
        final ChannelGroup channelGroup = getChannelGroup(platform, channelGroupId);
        for(final Channel channel : getChannelsByChannelGroup(channelGroup))
            removeChannel(channelGroup, channel.getId());
        channelGroupMapper.delete(channelGroup);
        return tenant;
    }

    public void delete(final Tenant tenant) {
        for(final ChannelGroup channelGroup : getChannelGroups(tenant))
            removeChannelGroup(tenant, channelGroup.getPlatform(), channelGroup.getId());
        tenantMapper.delete(tenant);
    }

    public class NoSuchChannelException extends Exception {
        NoSuchChannelException() {
            super();
        }

        NoSuchChannelException(final String message) {
            super(message);
        }
    }
}
