package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ChannelOuterClass;

/**
 * Created by cobi on 10/5/16.
 */
public abstract class Channel {
    public static Channel fromProto(ChannelOuterClass.Channel channel) {
        switch(channel.getPlatform()) {
            case TWITCH: return TwitchChannel.fromProto(channel);
            case DISCORD: return DiscordChannel.fromProto(channel);
            default: throw new IllegalStateException("Unknown channel platform " + channel.getPlatform().name());
        }
    }

    public static Channel fromProto(Tenant tenant, ChannelOuterClass.TenantEntry tenantEntry) {
        switch(tenantEntry.getPlatform()) {
            case TWITCH: return TwitchChannel.fromProto(tenant, tenantEntry);
            case DISCORD: return DiscordChannel.fromProto(tenant, tenantEntry);
            default: throw new IllegalStateException("Unknown channel platform " + tenantEntry.getPlatform().name());
        }
    }

    protected String id;
    protected Tenant tenant;
    protected String displayName;

    public Channel(String id, Tenant tenant, String displayName) {
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
