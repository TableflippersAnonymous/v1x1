package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ChannelOuterClass;

/**
 * Created by cobi on 10/5/16.
 */
public abstract class Channel {
    public static Channel fromProto(ChannelOuterClass.Channel channel) {
        switch(channel.getType()) {
            case TWITCH: return TwitchChannel.fromProto(channel);
            case DISCORD: return DiscordChannel.fromProto(channel);
            default: throw new IllegalStateException("Unknown channel type " + channel.getType().name());
        }
    }

    protected String name;

    protected Channel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract ChannelOuterClass.Channel toProto();
}
