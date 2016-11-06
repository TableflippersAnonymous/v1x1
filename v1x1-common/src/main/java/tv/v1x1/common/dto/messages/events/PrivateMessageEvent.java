package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.PrivateMessage;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Created by cobi on 11/4/2016.
 */
public abstract class PrivateMessageEvent extends Event {
    public static PrivateMessageEvent fromProto(final Module module, final UUID uuid, final long timestamp, final EventOuterClass.PrivateMessageEvent privateMessageEvent) {
        final PrivateMessage privateMessage = PrivateMessage.fromProto(privateMessageEvent.getPrivateMessage());
        switch(privateMessageEvent.getType()) {
            case TWITCH: return TwitchPrivateMessageEvent.fromProto(module, uuid, timestamp, privateMessage, privateMessageEvent.getExtension(EventOuterClass.TwitchPrivateMessageEvent.data));
            default: throw new IllegalStateException("Unknown PrivateMessageEvent type: " + privateMessageEvent.getType());
        }
    }

    private final PrivateMessage privateMessage;

    public PrivateMessageEvent(final Module from, final PrivateMessage privateMessage) {
        super(from);
        this.privateMessage = privateMessage;
    }

    public PrivateMessageEvent(final Module from, final UUID messageId, final long timestamp, final PrivateMessage privateMessage) {
        super(from, messageId, timestamp);
        this.privateMessage = privateMessage;
    }

    public PrivateMessage getPrivateMessage() {
        return privateMessage;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.PRIVATE_MESSAGE)
                .setExtension(EventOuterClass.PrivateMessageEvent.data, toProtoPrivateMessage().build());
    }

    protected EventOuterClass.PrivateMessageEvent.Builder toProtoPrivateMessage() {
        return EventOuterClass.PrivateMessageEvent.newBuilder()
                .setPrivateMessage(privateMessage.toProto());
    }
}
