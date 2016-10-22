package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.db.Platform;
import tv.twitchbot.common.dto.proto.core.ChannelOuterClass;
import tv.twitchbot.common.dto.proto.core.PlatformOuterClass;

/**
 * Represents a {@link Channel} on the Twitch platform
 * @author Naomi
 */
public class TwitchChannel extends Channel {
    public static TwitchChannel fromProto(final ChannelOuterClass.Channel channel) {
        final String id = channel.getId();
        final Tenant tenant = Tenant.fromProto(channel.getTenant());
        final String displayName = channel.getDisplayName();
        return new TwitchChannel(id, tenant, displayName);
    }

    public static TwitchChannel fromProto(final Tenant tenant, final ChannelOuterClass.TenantEntry tenantEntry) {
        final String id = tenantEntry.getId();
        final String displayName = tenantEntry.getDisplayName();
        return new TwitchChannel(id, tenant, displayName);
    }

    public TwitchChannel(final String id, final Tenant tenant, final String displayName) {
        super(id, tenant, displayName);
    }

    @Override
    public ChannelOuterClass.Channel toProto() {
        return ChannelOuterClass.Channel.newBuilder()
                .setId(id)
                .setDisplayName(displayName)
                .setTenant(tenant.toProto())
                .setPlatform(PlatformOuterClass.Platform.TWITCH)
                .build();
    }

    @Override
    public ChannelOuterClass.TenantEntry toProtoTenantEntry() {
        return ChannelOuterClass.TenantEntry.newBuilder()
                .setId(id)
                .setDisplayName(displayName)
                .setPlatform(PlatformOuterClass.Platform.TWITCH)
                .build();
    }

    @Override
    public tv.twitchbot.common.dto.db.Channel toDB() {
        return new tv.twitchbot.common.dto.db.TwitchChannel(id, displayName, tenant.getId().getValue());
    }

    @Override
    public tv.twitchbot.common.dto.db.Tenant.Entry toDBTenant() {
        return new tv.twitchbot.common.dto.db.Tenant.Entry(Platform.TWITCH, displayName, id);
    }


}
