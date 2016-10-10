package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.ChatMessage;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.PrivmsgCommand;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Created by cobi on 10/9/2016.
 */
public class TwitchChatMessageEvent extends ChatMessageEvent {
    public static TwitchChatMessageEvent fromProto(Module module, UUID uuid, long timestamp, ChatMessage chatMessage, EventOuterClass.TwitchChatMessageEvent twitchChatMessageEvent) {
        PrivmsgCommand privmsgCommand = (PrivmsgCommand) IrcStanza.fromProto(twitchChatMessageEvent.getPrivmsgCommand());
        return new TwitchChatMessageEvent(module, uuid, timestamp, chatMessage, privmsgCommand);
    }

    private PrivmsgCommand privmsgCommand;

    public TwitchChatMessageEvent(Module from, ChatMessage chatMessage, PrivmsgCommand privmsgCommand) {
        super(from, chatMessage);
        this.privmsgCommand = privmsgCommand;
    }

    public TwitchChatMessageEvent(Module from, UUID messageId, long timestamp, ChatMessage chatMessage, PrivmsgCommand privmsgCommand) {
        super(from, messageId, timestamp, chatMessage);
        this.privmsgCommand = privmsgCommand;
    }

    public PrivmsgCommand getPrivmsgCommand() {
        return privmsgCommand;
    }

    @Override
    protected EventOuterClass.ChatMessageEvent.Builder toProtoChatMessage() {
        return super.toProtoChatMessage()
                .setType(EventOuterClass.ChatMessageEvent.Type.TWITCH)
                .setExtension(EventOuterClass.TwitchChatMessageEvent.data, EventOuterClass.TwitchChatMessageEvent.newBuilder()
                        .setPrivmsgCommand(privmsgCommand.toProto())
                        .build()
                );
    }
}
