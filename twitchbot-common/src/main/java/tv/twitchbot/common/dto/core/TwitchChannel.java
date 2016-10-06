package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ChannelOuterClass;

/**
 * Created by naomi on 10/5/16.
 */
public class TwitchChannel extends Channel {
    public static TwitchChannel fromProto(ChannelOuterClass.Channel channel) {
        return new TwitchChannel(channel.getName());
    }

    public TwitchChannel(String name) {
        super(name);
    }

    @Override
    public ChannelOuterClass.Channel toProto() {
        return ChannelOuterClass.Channel.newBuilder()
                .setName(name)
                .setType(ChannelOuterClass.Channel.ChannelType.TWITCH)
                .build();
    }
}
