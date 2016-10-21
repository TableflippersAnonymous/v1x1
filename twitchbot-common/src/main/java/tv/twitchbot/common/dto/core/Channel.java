package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ChannelOuterClass;

/**
 * Represents a single text chat room on a platform on which the bot is a part of
 * @author Naomi
 */
public abstract class Channel {
    public static Channel fromProto(final ChannelOuterClass.Channel channel) {
        switch(channel.getPlatform()) {
            case TWITCH: return TwitchChannel.fromProto(channel);
            case DISCORD: return DiscordChannel.fromProto(channel);
            default: throw new IllegalStateException("Unknown channel platform " + channel.getPlatform().name());
        }
    }

    public static Channel fromProto(final Tenant tenant, final ChannelOuterClass.TenantEntry tenantEntry) {
        switch(tenantEntry.getPlatform()) {
            case TWITCH: return TwitchChannel.fromProto(tenant, tenantEntry);
            case DISCORD: return DiscordChannel.fromProto(tenant, tenantEntry);
            default: throw new IllegalStateException("Unknown channel platform " + tenantEntry.getPlatform().name());
        }
    }

    protected String id;
    protected Tenant tenant;
    protected String displayName;

    public Channel(final String id, final Tenant tenant, final String displayName) {
        this.id = id;
        this.tenant = tenant;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public abstract ChannelOuterClass.Channel toProto();
    public abstract ChannelOuterClass.TenantEntry toProtoTenantEntry();

    public abstract tv.twitchbot.common.dto.db.Channel toDB();
    public abstract tv.twitchbot.common.dto.db.Tenant.Entry toDBTenant();
}
