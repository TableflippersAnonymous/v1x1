package tv.v1x1.common.dto.core;

import tv.v1x1.common.dto.proto.core.ChannelOuterClass;

/**
 * Represents a {@link Channel} on the Discord platform
 * @author Naomi
 */
public class DiscordChannel extends Channel {
    public static DiscordChannel fromProto(final ChannelOuterClass.Channel channel) {
        final String id = channel.getId();
        final DiscordGuild guild = DiscordGuild.fromProto(channel.getChannelGroup());
        final String displayName = channel.getDisplayName();
        return new DiscordChannel(id, guild, displayName);
    }

    public static DiscordChannel fromProto(final ChannelGroup guild, final ChannelOuterClass.ChannelGroupEntry channelGroupEntry) {
        final String id = channelGroupEntry.getId();
        final String displayName = channelGroupEntry.getDisplayName();
        return new DiscordChannel(id, guild, displayName);
    }

    public DiscordChannel(final String id, final ChannelGroup guild, final String displayName) {
        super(id, guild, displayName);
    }
}
