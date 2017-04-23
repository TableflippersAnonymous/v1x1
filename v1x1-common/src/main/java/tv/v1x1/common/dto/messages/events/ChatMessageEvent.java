package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when the ingestion of a chat message from a platform is complete
 * @author Cobi
 */
public abstract class ChatMessageEvent extends Event {
    public static ChatMessageEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.ChatMessageEvent chatMessageEvent) {
        final ChatMessage chatMessage = ChatMessage.fromProto(chatMessageEvent.getChatMessage());
        switch(chatMessageEvent.getType()) {
            case TWITCH: return TwitchChatMessageEvent.fromProto(module, uuid, timestamp, context, chatMessage, chatMessageEvent.getExtension(EventOuterClass.TwitchChatMessageEvent.data));
            default: throw new IllegalStateException("Unknown ChatMessageEvent type: " + chatMessageEvent.getType());
        }
    }

    private final ChatMessage chatMessage;

    public ChatMessageEvent(final Module from, final ChatMessage chatMessage) {
        super(from);
        this.chatMessage = chatMessage;
    }

    public ChatMessageEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final ChatMessage chatMessage) {
        super(from, messageId, timestamp, context);
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
