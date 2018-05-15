package tv.v1x1.common.dto.core;

import tv.v1x1.common.dto.proto.core.ChannelOuterClass;

/**
 * Represents a {@link Channel} on the Twitch platform
 * @author Naomi
 */
public class TwitchChannel extends Channel {
    public static final TwitchChannel EMPTY = new TwitchChannel(null, null, null);

    public static TwitchChannel fromProto(final ChannelOuterClass.Channel channel) {
        final String id = channel.getId();
        final ChannelGroup channelGroup = ChannelGroup.fromProto(channel.getChannelGroup());
        final String displayName = channel.getDisplayName();
        return new TwitchChannel(id, channelGroup, displayName);
    }

    public static TwitchChannel fromProto(final ChannelGroup channelGroup, final ChannelOuterClass.ChannelGroupEntry channelGroupEntry) {
        final String id = channelGroupEntry.getId();
        final String displayName = channelGroupEntry.getDisplayName();
        return new TwitchChannel(id, channelGroup, displayName);
    }

    public TwitchChannel(final String id, final ChannelGroup channelGroup, final String displayName) {
        super(id, channelGroup, displayName);
    }
}
