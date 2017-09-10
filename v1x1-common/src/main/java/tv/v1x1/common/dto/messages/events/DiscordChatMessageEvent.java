package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.proto.core.PlatformOuterClass;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Created by naomi on 4/13/2018.
 */
public class DiscordChatMessageEvent extends ChatMessageEvent {
    public static DiscordChatMessageEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final ChatMessage chatMessage) {
        return new DiscordChatMessageEvent(module, uuid, timestamp, context, chatMessage);
    }

    public DiscordChatMessageEvent(final Module from, final ChatMessage chatMessage) {
        super(from, chatMessage);
    }

    public DiscordChatMessageEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final ChatMessage chatMessage) {
        super(from, messageId, timestamp, context, chatMessage);
    }

    @Override
    protected EventOuterClass.ChatMessageEvent.Builder toProtoChatMessage() {
        return super.toProtoChatMessage()
                .setType(PlatformOuterClass.Platform.DISCORD);
    }
}
