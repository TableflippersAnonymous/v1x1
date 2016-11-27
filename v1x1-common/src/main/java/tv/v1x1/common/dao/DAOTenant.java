package tv.v1x1.common.dao;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import tv.v1x1.common.dto.db.Channel;
import tv.v1x1.common.dto.db.DiscordChannel;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.dto.db.TwitchChannel;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Sends/Retrieves Tenants to/from the database
 * @author Naomi
 */
public class DAOTenant {
    private final Session session;
    private final Mapper<Tenant> tenantMapper;
    private final Mapper<DiscordChannel> discordChannelMapper;
    private final Mapper<TwitchChannel> twitchChannelMapper;

    public DAOTenant(final MappingManager mappingManager) {
        this.session = mappingManager.getSession();
        this.tenantMapper = mappingManager.mapper(Tenant.class);
        this.discordChannelMapper = mappingManager.mapper(DiscordChannel.class);
        this.twitchChannelMapper = mappingManager.mapper(TwitchChannel.class);
    }

    public Tenant getById(final UUID id) {
        return tenantMapper.get(id);
    }

    public Channel getChannel(final Platform platform, final String channelId) {
        switch(platform) {
            case DISCORD: return discordChannelMapper.get(channelId);
            case TWITCH: return twitchChannelMapper.get(channelId);
            default: throw new IllegalStateException("Unknown channel platform: " + platform);
        }
    }

    public Tenant getByChannel(final Platform platform, final String channelId) {
        return getByChannel(getChannel(platform, channelId));
    }

    public Tenant getByChannel(final Channel channel) {
        if(channel == null)
            return null;
        return getById(channel.getTenantId());
    }

    public Tenant getOrCreate(final Platform platform, final String channelId, final String displayName) {
        final Channel channel = getChannel(platform, channelId);
        if(channel == null)
            return createTenant(platform, channelId, displayName);
        final Tenant tenant = getById(channel.getTenantId());
        if(tenant == null)
            throw new IllegalStateException("Tenant null but inverse_tenant for: " + channel.getTenantId().toString() + " " + channel.getId() + " " + channel.getClass().getCanonicalName());
        return tenant;
    }

    public Tenant createTenant(final Platform platform, final String channelId, final String displayName) {
        final Tenant tenant = new Tenant(UUID.randomUUID(), new ArrayList<>());
        tenant.getEntries().add(new Tenant.Entry(platform, displayName, channelId));
        final BatchStatement b = new BatchStatement();
        b.add(tenantMapper.saveQuery(tenant));
        switch(platform) {
            case DISCORD:
                b.add(discordChannelMapper.saveQuery(new DiscordChannel(channelId, displayName, tenant.getId())));
                break;
            case TWITCH:
                b.add(twitchChannelMapper.saveQuery(new TwitchChannel(channelId, displayName, tenant.getId())));
                break;
            default:
                throw new IllegalStateException("Unknown channel platform: " + platform);
        }
        session.execute(b);
        return tenant;
    }

    public Tenant addChannel(final Tenant tenant, final Platform platform, final String channelId, final String displayName) {
        tenant.getEntries().add(new Tenant.Entry(platform, displayName, channelId));
        final BatchStatement b = new BatchStatement();
        b.add(tenantMapper.saveQuery(tenant));
        switch(platform) {
            case DISCORD:
                b.add(discordChannelMapper.saveQuery(new DiscordChannel(channelId, displayName, tenant.getId())));
                break;
            case TWITCH:
                b.add(twitchChannelMapper.saveQuery(new TwitchChannel(channelId, displayName, tenant.getId())));
                break;
            default:
                throw new IllegalStateException("Unknown channel platform: " + platform);
        }
        session.execute(b);
        return tenant;
    }

    public Tenant removeChannel(final Tenant tenant, final Platform platform, final String channelId) {
        final BatchStatement b = new BatchStatement();
        if(tenant.getEntries().removeIf(entry -> entry.getPlatform() == platform && entry.getChannelId().equals(channelId)))
            b.add(tenantMapper.saveQuery(tenant));
        final Channel channel = getChannel(platform, channelId);
        if(channel != null)
            switch(platform) {
                case DISCORD:
                    b.add(discordChannelMapper.deleteQuery(channel));
                    break;
                case TWITCH:
                    b.add(twitchChannelMapper.deleteQuery(channel));
                    break;
                default:
                    throw new IllegalStateException("Unknown channel platform: " + platform);
            }
        if(b.size() > 0)
            session.execute(b);
        return tenant;
    }

    public void delete(final Tenant tenant) {
        final BatchStatement b = new BatchStatement();
        b.add(tenantMapper.deleteQuery(tenant));
        for(final Tenant.Entry entry : tenant.getEntries()) {
            final Channel channel = getChannel(entry.getPlatform(), entry.getChannelId());
            if(channel != null)
                switch(entry.getPlatform()) {
                    case DISCORD:
                        b.add(discordChannelMapper.deleteQuery(channel));
                        break;
                    case TWITCH:
                        b.add(twitchChannelMapper.deleteQuery(channel));
                        break;
                    default:
                        throw new IllegalStateException("Unknown channel platform: " + entry.getPlatform());
                }
        }
        session.execute(b);
    }
}
