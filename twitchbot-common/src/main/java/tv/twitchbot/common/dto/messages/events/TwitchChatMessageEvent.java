package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.ChatMessage;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.PrivmsgCommand;
import tv.twitchbot.common.dto.proto.core.PlatformOuterClass;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link tv.twitchbot.common.dto.core.TwitchUser} sends a message to a {@link tv.twitchbot.common.dto.core.TwitchChannel} we've ingested
 * @author Naomi
 */
public class TwitchChatMessageEvent extends ChatMessageEvent {
    public static TwitchChatMessageEvent fromProto(final Module module, final UUID uuid, final long timestamp, final ChatMessage chatMessage, final EventOuterClass.TwitchChatMessageEventOrBuilder twitchChatMessageEvent) {
        final PrivmsgCommand privmsgCommand = (PrivmsgCommand) IrcStanza.fromProto(twitchChatMessageEvent.getPrivmsgCommand());
        return new TwitchChatMessageEvent(module, uuid, timestamp, chatMessage, privmsgCommand);
    }

    private final PrivmsgCommand privmsgCommand;

    public TwitchChatMessageEvent(final Module from, final ChatMessage chatMessage, final PrivmsgCommand privmsgCommand) {
        super(from, chatMessage);
        this.privmsgCommand = privmsgCommand;
    }

    public TwitchChatMessageEvent(final Module from, final UUID messageId, final long timestamp, final ChatMessage chatMessage, final PrivmsgCommand privmsgCommand) {
        super(from, messageId, timestamp, chatMessage);
        this.privmsgCommand = privmsgCommand;
    }

    public PrivmsgCommand getPrivmsgCommand() {
        return privmsgCommand;
    }

    @Override
    protected EventOuterClass.ChatMessageEvent.Builder toProtoChatMessage() {
        return super.toProtoChatMessage()
                .setType(PlatformOuterClass.Platform.TWITCH)
                .setExtension(EventOuterClass.TwitchChatMessageEvent.data, EventOuterClass.TwitchChatMessageEvent.newBuilder()
                        .setPrivmsgCommand(privmsgCommand.toProto())
                        .build()
                );
    }
}
