package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ChannelOuterClass;

/**
 * Created by cobi on 10/5/16.
 */
public class TwitchChannel extends Channel {
    public static TwitchChannel fromProto(ChannelOuterClass.Channel channel) {
        String name = channel.getName();
        Tenant tenant = Tenant.fromProto(channel.getTenant());
        return new TwitchChannel(name, tenant);
    }

    public TwitchChannel(String name, Tenant tenant) {
        super(name, tenant);
    }

    @Override
    public ChannelOuterClass.Channel toProto() {
        return ChannelOuterClass.Channel.newBuilder()
                .setName(name)
                .setType(ChannelOuterClass.Channel.ChannelType.TWITCH)
                .setTenant(tenant.toProto())
                .build();
    }

    @Override
    public tv.twitchbot.common.dto.db.TwitchChannel toDB() {
        return new tv.twitchbot.common.dto.db.TwitchChannel(getName(), getTenant().getId().getValue());
    }
}
