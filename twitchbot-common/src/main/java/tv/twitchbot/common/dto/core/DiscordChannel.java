package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.db.Platform;
import tv.twitchbot.common.dto.proto.core.ChannelOuterClass;
import tv.twitchbot.common.dto.proto.core.PlatformOuterClass;

/**
 * Represents a {@link Channel} on the Discord platform
 * @author Cobi
 */
public class DiscordChannel extends Channel {
    public static DiscordChannel fromProto(final ChannelOuterClass.Channel channel) {
        final String id = channel.getId();
        final Tenant tenant = Tenant.fromProto(channel.getTenant());
        final String displayName = channel.getDisplayName();
        return new DiscordChannel(id, tenant, displayName);
    }

    public static DiscordChannel fromProto(final Tenant tenant, final ChannelOuterClass.TenantEntry tenantEntry) {
        final String id = tenantEntry.getId();
        final String displayName = tenantEntry.getDisplayName();
        return new DiscordChannel(id, tenant, displayName);
    }

    public DiscordChannel(final String id, final Tenant tenant, final String displayName) {
        super(id, tenant, displayName);
    }

    @Override
    public ChannelOuterClass.Channel toProto() {
        return ChannelOuterClass.Channel.newBuilder()
                .setId(id)
                .setDisplayName(displayName)
                .setTenant(tenant.toProto())
                .setPlatform(PlatformOuterClass.Platform.DISCORD)
                .build();
    }

    @Override
    public ChannelOuterClass.TenantEntry toProtoTenantEntry() {
        return ChannelOuterClass.TenantEntry.newBuilder()
                .setId(id)
                .setDisplayName(displayName)
                .setPlatform(PlatformOuterClass.Platform.DISCORD)
                .build();
    }

    @Override
    public tv.twitchbot.common.dto.db.Channel toDB() {
        return new tv.twitchbot.common.dto.db.DiscordChannel(id, displayName, tenant.getId().getValue());
    }

    @Override
    public tv.twitchbot.common.dto.db.Tenant.Entry toDBTenant() {
        return new tv.twitchbot.common.dto.db.Tenant.Entry(Platform.DISCORD, displayName, id);
    }
}
