package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.db.Platform;
import tv.twitchbot.common.dto.proto.core.ChannelOuterClass;
import tv.twitchbot.common.dto.proto.core.PlatformOuterClass;

/**
 * Created by naomi on 10/5/16.
 */
public class TwitchChannel extends Channel {
    public static TwitchChannel fromProto(ChannelOuterClass.Channel channel) {
        String id = channel.getId();
        Tenant tenant = Tenant.fromProto(channel.getTenant());
        String displayName = channel.getDisplayName();
        return new TwitchChannel(id, tenant, displayName);
    }

    public static TwitchChannel fromProto(Tenant tenant, ChannelOuterClass.TenantEntry tenantEntry) {
        String id = tenantEntry.getId();
        String displayName = tenantEntry.getDisplayName();
        return new TwitchChannel(id, tenant, displayName);
    }

    public TwitchChannel(String id, Tenant tenant, String displayName) {
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
