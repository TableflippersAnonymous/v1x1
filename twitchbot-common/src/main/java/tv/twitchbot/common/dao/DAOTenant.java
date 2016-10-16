package tv.twitchbot.common.dao;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import tv.twitchbot.common.dto.db.*;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by naomi on 10/16/2016.
 */
public class DAOTenant {
    private Session session;
    private Mapper<Tenant> tenantMapper;
    private Mapper<DiscordChannel> discordChannelMapper;
    private Mapper<TwitchChannel> twitchChannelMapper;

    public DAOTenant(MappingManager mappingManager) {
        this.session = mappingManager.getSession();
        this.tenantMapper = mappingManager.mapper(Tenant.class);
        this.discordChannelMapper = mappingManager.mapper(DiscordChannel.class);
        this.twitchChannelMapper = mappingManager.mapper(TwitchChannel.class);
    }

    public Tenant getById(UUID id) {
        return tenantMapper.get(id);
    }

    public Channel getChannel(Platform platform, String channelId) {
        switch(platform) {
            case DISCORD: return discordChannelMapper.get(channelId);
            case TWITCH: return twitchChannelMapper.get(channelId);
            default: throw new IllegalStateException("Unknown channel platform: " + platform);
        }
    }

    public Tenant getByChannel(Platform platform, String channelId) {
        return getByChannel(getChannel(platform, channelId));
    }

    public Tenant getByChannel(Channel channel) {
        if(channel == null)
            return null;
        return getById(channel.getTenantId());
    }

    public Tenant getOrCreate(Platform platform, String channelId, String displayName) {
        Channel channel = getChannel(platform, channelId);
        if(channel != null) {
            Tenant tenant = getById(channel.getTenantId());
            if(tenant == null)
                throw new IllegalStateException("Tenant null but inverse_tenant for: " + channel.getTenantId().toString() + " " + channel.getId() + " " + channel.getClass().getCanonicalName());
            return tenant;
        } else {
            return createTenant(platform, channelId, displayName);
        }
    }

    public Tenant createTenant(Platform platform, String channelId, String displayName) {
        Tenant tenant = new Tenant(UUID.randomUUID(), new ArrayList<>());
        tenant.getEntries().add(new Tenant.Entry(platform, displayName, channelId));
        BatchStatement b = new BatchStatement();
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

    public Tenant addChannel(Tenant tenant, Platform platform, String channelId, String displayName) {
        tenant.getEntries().add(new Tenant.Entry(platform, displayName, channelId));
        BatchStatement b = new BatchStatement();
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

    public Tenant removeChannel(Tenant tenant, Platform platform, String channelId) {
        BatchStatement b = new BatchStatement();
        if(tenant.getEntries().removeIf(entry -> entry.getPlatform() == platform && entry.getChannelId().equals(channelId)))
            b.add(tenantMapper.saveQuery(tenant));
        Channel channel = getChannel(platform, channelId);
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

    public void delete(Tenant tenant) {
        BatchStatement b = new BatchStatement();
        b.add(tenantMapper.deleteQuery(tenant));
        for(Tenant.Entry entry : tenant.getEntries()) {
            Channel channel = getChannel(entry.getPlatform(), entry.getChannelId());
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
