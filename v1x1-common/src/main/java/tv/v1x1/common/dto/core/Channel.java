package tv.v1x1.common.dto.core;

import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;

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

    public abstract Platform getPlatform();

    public abstract ChannelOuterClass.Channel toProto();
    public abstract ChannelOuterClass.TenantEntry toProtoTenantEntry();

    public abstract tv.v1x1.common.dto.db.Channel toDB();
    public abstract tv.v1x1.common.dto.db.Tenant.Entry toDBTenant();
}
