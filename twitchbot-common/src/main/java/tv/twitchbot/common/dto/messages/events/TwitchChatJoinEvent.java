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
 * @author Cobi
 */
public class TwitchChatJoinEvent extends ChatJoinEvent {
    public static TwitchChatJoinEvent fromProto(Module module, UUID uuid, long timestamp, User user, Channel channel, EventOuterClass.TwitchChatJoinEvent twitchChatJoinEvent) {
        JoinCommand joinCommand = (JoinCommand) IrcStanza.fromProto(twitchChatJoinEvent.getJoinCommand());
        return new TwitchChatJoinEvent(module, uuid, timestamp, user, channel, joinCommand);
    }

    private JoinCommand joinCommand;

    public TwitchChatJoinEvent(Module from, User user, Channel channel, JoinCommand joinCommand) {
        super(from, user, channel);
        this.joinCommand = joinCommand;
    }

    public TwitchChatJoinEvent(Module from, UUID messageId, long timestamp, User user, Channel channel, JoinCommand joinCommand) {
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
