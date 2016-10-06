package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ChannelOuterClass;

/**
 * Created by cobi on 10/5/16.
 */
public class DiscordChannel extends Channel {
    public static DiscordChannel fromProto(ChannelOuterClass.Channel channel) {
        return new DiscordChannel(channel.getName());
    }

    public DiscordChannel(String name) {
        super(name);
    }

    @Override
    public ChannelOuterClass.Channel toProto() {
        return ChannelOuterClass.Channel.newBuilder()
                .setName(name)
                .setType(ChannelOuterClass.Channel.ChannelType.DISCORD)
                .build();
    }
}
