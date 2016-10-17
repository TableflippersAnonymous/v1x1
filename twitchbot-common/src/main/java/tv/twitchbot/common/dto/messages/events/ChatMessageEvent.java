package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.ChatMessage;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when the ingestion of a chat message from a platform is complete
 * @author Naomi
 */
public abstract class ChatMessageEvent extends Event {
    public static ChatMessageEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.ChatMessageEvent chatMessageEvent) {
        ChatMessage chatMessage = ChatMessage.fromProto(chatMessageEvent.getChatMessage());
        switch(chatMessageEvent.getType()) {
            case TWITCH: return TwitchChatMessageEvent.fromProto(module, uuid, timestamp, chatMessage, chatMessageEvent.getExtension(EventOuterClass.TwitchChatMessageEvent.data));
            default: throw new IllegalStateException("Unknown ChatMessageEvent type: " + chatMessageEvent.getType());
        }
    }

    private ChatMessage chatMessage;

    public ChatMessageEvent(Module from, ChatMessage chatMessage) {
        super(from);
        this.chatMessage = chatMessage;
    }

    public ChatMessageEvent(Module from, UUID messageId, long timestamp, ChatMessage chatMessage) {
        super(from, messageId, timestamp);
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.CHAT_MESSAGE)
                .setExtension(EventOuterClass.ChatMessageEvent.data, toProtoChatMessage().build());
    }

    protected EventOuterClass.ChatMessageEvent.Builder toProtoChatMessage() {
        return EventOuterClass.ChatMessageEvent.newBuilder()
                .setChatMessage(chatMessage.toProto());
    }
}
