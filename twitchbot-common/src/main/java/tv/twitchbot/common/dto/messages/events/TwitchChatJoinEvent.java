package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.Channel;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.core.User;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.JoinCommand;
import tv.twitchbot.common.dto.proto.core.PlatformOuterClass;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link tv.twitchbot.common.dto.core.TwitchUser} joins a {@link tv.twitchbot.common.dto.core.TwitchChannel}
 * @author Naomi
 */
public class TwitchChatJoinEvent extends ChatJoinEvent {
    public static TwitchChatJoinEvent fromProto(final Module module, final UUID uuid, final long timestamp, final User user, final Channel channel, final EventOuterClass.TwitchChatJoinEventOrBuilder twitchChatJoinEvent) {
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
