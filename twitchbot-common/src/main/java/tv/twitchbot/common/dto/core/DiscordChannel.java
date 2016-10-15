package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ChannelOuterClass;

/**
 * Created by naomi on 10/5/16.
 */
public class DiscordChannel extends Channel {
    public static DiscordChannel fromProto(ChannelOuterClass.Channel channel) {
        String name = channel.getName();
        Tenant tenant = Tenant.fromProto(channel.getTenant());
        return new DiscordChannel(name, tenant);
    }

    public DiscordChannel(String name, Tenant tenant) {
        super(name, tenant);
    }

    @Override
    public ChannelOuterClass.Channel toProto() {
        return ChannelOuterClass.Channel.newBuilder()
                .setName(name)
                .setType(ChannelOuterClass.Channel.ChannelType.DISCORD)
                .setTenant(tenant.toProto())
                .build();
    }

    @Override
    public tv.twitchbot.common.dto.db.DiscordChannel toDB() {
        return new tv.twitchbot.common.dto.db.DiscordChannel(getName(), getTenant().getId().getValue());
    }
}
