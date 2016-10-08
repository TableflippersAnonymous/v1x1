package tv.twitchbot.common.dto.messages;

import com.google.protobuf.InvalidProtocolBufferException;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;
import tv.twitchbot.common.dto.proto.messages.MessageOuterClass;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

import java.util.Date;

/**
 * Created by naomi on 10/4/16.
 */
public abstract class Message {
    public static Message fromBytes(byte[] bytes) throws InvalidProtocolBufferException {
        return fromProto(MessageOuterClass.Message.parseFrom(bytes));
    }

    public static Message fromProto(MessageOuterClass.Message message) {
        Module module = Module.fromProto(message.getFrom());
        UUID uuid = UUID.fromProto(message.getMessageId());
        long timestamp = message.getTimestamp();
        switch(message.getType()) {
            case EVENT: return Event.fromProto(module, uuid, timestamp, message.getExtension(EventOuterClass.Event.data));
            case REQUEST: return Request.fromProto(module, uuid, timestamp, message.getExtension(RequestOuterClass.Request.data));
            case RESPONSE: return Response.fromProto(module, uuid, timestamp, message.getExtension(RequestOuterClass.Response.data));
            default: throw new IllegalStateException("Unknown message type " + message.getType().name());
        }
    }

    private Module from;
    private UUID messageId;
    private long timestamp;

    public Message(Module from) {
        this(from, new UUID(java.util.UUID.randomUUID()), new Date().getTime());
    }

    public Message(Module from, UUID messageId, long timestamp) {
        this.from = from;
        this.messageId = messageId;
        this.timestamp = timestamp;
    }

    public Module getFrom() {
        return from;
    }

    /**
     * Represents the (queue) message ID dropped onto the message queue
     * @return UUID the message ID
     */
    public UUID getMessageId() {
        return messageId;
    }

    /**
     * Represents the timestamp the (queue) message was created locally
     * @return long the timestamp we put it on
     */
    public long getTimestamp() {
        return timestamp;
    }

    public MessageOuterClass.Message toProto() {
        return toProtoMessage().build();
    }

    protected MessageOuterClass.Message.Builder toProtoMessage() {
        return MessageOuterClass.Message.newBuilder()
                .setFrom(from.toProto())
                .setMessageId(messageId.toProto())
                .setTimestamp(timestamp);
    }

    public byte[] toBytes() {
        return toProto().toByteArray();
    }
}
