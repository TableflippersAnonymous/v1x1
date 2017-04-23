package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.PrivmsgCommand;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.proto.core.PlatformOuterClass;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link tv.v1x1.common.dto.core.TwitchUser} sends a message to a {@link tv.v1x1.common.dto.core.TwitchChannel} we've ingested
 * @author Cobi
 */
public class TwitchChatMessageEvent extends ChatMessageEvent {
    public static TwitchChatMessageEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final ChatMessage chatMessage, final EventOuterClass.TwitchChatMessageEvent twitchChatMessageEvent) {
        final PrivmsgCommand privmsgCommand = (PrivmsgCommand) IrcStanza.fromProto(twitchChatMessageEvent.getPrivmsgCommand());
        return new TwitchChatMessageEvent(module, uuid, timestamp, context, chatMessage, privmsgCommand);
    }

    private final PrivmsgCommand privmsgCommand;

    public TwitchChatMessageEvent(final Module from, final ChatMessage chatMessage, final PrivmsgCommand privmsgCommand) {
        super(from, chatMessage);
        this.privmsgCommand = privmsgCommand;
    }

    public TwitchChatMessageEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final ChatMessage chatMessage, final PrivmsgCommand privmsgCommand) {
        super(from, messageId, timestamp, context, chatMessage);
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
