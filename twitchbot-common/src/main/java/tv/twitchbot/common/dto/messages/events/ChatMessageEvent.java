package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.ChatMessage;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Created by cobi on 10/5/16.
 */
public class ChatMessageEvent extends Event {
    public static ChatMessageEvent fromProto(Module module, EventOuterClass.ChatMessageEvent chatMessageEvent) {
        ChatMessage chatMessage = ChatMessage.fromProto(chatMessageEvent.getChatMessage());
        return new ChatMessageEvent(module, chatMessage);
    }

    private ChatMessage chatMessage;

    public ChatMessageEvent(Module module, ChatMessage chatMessage) {
        super(module);
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.CHAT_MESSAGE)
                .setExtension(EventOuterClass.ChatMessageEvent.data, EventOuterClass.ChatMessageEvent.newBuilder().build());
    }
}
