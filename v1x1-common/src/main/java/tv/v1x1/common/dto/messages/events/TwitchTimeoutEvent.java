package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.*;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.ClearChatCommand;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link TwitchUser} has been timed out and chat purged or when all chat for a channel has been purged
 * @author Cobi
 * @see <a href="https://github.com/justintv/Twitch-API/blob/master/IRC.md#clearchat-1">Twitch-API Documentation</a>
 */
public class TwitchTimeoutEvent extends Event {
    public static TwitchTimeoutEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.TwitchTimeoutEvent twitchTimeoutEvent) {
        final TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchTimeoutEvent.getChannel());
        final TwitchUser user = (TwitchUser) User.fromProto(twitchTimeoutEvent.getUser());
        final ClearChatCommand clearChatCommand = (ClearChatCommand) IrcStanza.fromProto(twitchTimeoutEvent.getClearChatCommand());
        return new TwitchTimeoutEvent(module, uuid, timestamp, context, channel, user, clearChatCommand);
    }

    private final TwitchChannel channel;
    private final TwitchUser user;

    private final ClearChatCommand clearChatCommand;

    public TwitchTimeoutEvent(final Module from, final TwitchChannel channel, final TwitchUser user, final ClearChatCommand clearChatCommand) {
        super(from);
        this.channel = channel;
        this.user = user;
        this.clearChatCommand = clearChatCommand;
    }

    public TwitchTimeoutEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final TwitchChannel channel, final TwitchUser user, final ClearChatCommand clearChatCommand) {
        super(from, messageId, timestamp, context);
        this.channel = channel;
        this.user = user;
        this.clearChatCommand = clearChatCommand;
    }

    public TwitchChannel getChannel() {
        return channel;
    }

    public TwitchUser getUser() {
        return user;
    }

    public ClearChatCommand getClearChatCommand() {
        return clearChatCommand;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_TIMEOUT)
                .setExtension(EventOuterClass.TwitchTimeoutEvent.data, EventOuterClass.TwitchTimeoutEvent.newBuilder()
                        .setChannel(channel.toProto())
                        .setUser(user.toProto())
                        .setClearChatCommand(clearChatCommand.toProto())
                        .build()
                );
    }
}
