package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ChannelOuterClass;

/**
 * Created by naomi on 10/5/16.
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
    protected Tenant tenant;

    public Channel(String name, Tenant tenant) {
        this.name = name;
        this.tenant = tenant;
    }

    public String getName() {
        return name;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public abstract ChannelOuterClass.Channel toProto();

    public abstract tv.twitchbot.common.dto.db.Channel toDB();
}
