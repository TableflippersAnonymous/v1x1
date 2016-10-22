package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.JoinCommand;
import tv.v1x1.common.dto.proto.core.PlatformOuterClass;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link tv.v1x1.common.dto.core.TwitchUser} joins a {@link tv.v1x1.common.dto.core.TwitchChannel}
 * @author Cobi
 */
public class TwitchChatJoinEvent extends ChatJoinEvent {
    public static TwitchChatJoinEvent fromProto(final Module module, final UUID uuid, final long timestamp, final User user, final Channel channel, final EventOuterClass.TwitchChatJoinEvent twitchChatJoinEvent) {
        final JoinCommand joinCommand = (JoinCommand) IrcStanza.fromProto(twitchChatJoinEvent.getJoinCommand());
        return new TwitchChatJoinEvent(module, uuid, timestamp, user, channel, joinCommand);
    }

    private final JoinCommand joinCommand;

    public TwitchChatJoinEvent(final Module from, final User user, final Channel channel, final JoinCommand joinCommand) {
        super(from, user, channel);
        this.joinCommand = joinCommand;
    }

    public TwitchChatJoinEvent(final Module from, final UUID messageId, final long timestamp, final User user, final Channel channel, final JoinCommand joinCommand) {
        super(from, messageId, timestamp, user, channel);
        this.joinCommand = joinCommand;
    }

    public JoinCommand getJoinCommand() {
        return joinCommand;
    }

    @Override
    protected EventOuterClass.ChatJoinEvent.Builder toProtoChatJoin() {
        return super.toProtoChatJoin()
                .setType(PlatformOuterClass.Platform.TWITCH)
                .setExtension(EventOuterClass.TwitchChatJoinEvent.data, EventOuterClass.TwitchChatJoinEvent.newBuilder()
                        .setJoinCommand(joinCommand.toProto())
                        .build()
                );
    }
}
