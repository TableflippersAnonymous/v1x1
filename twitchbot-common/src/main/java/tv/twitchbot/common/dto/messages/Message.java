package tv.twitchbot.common.dto.messages;

import com.google.protobuf.InvalidProtocolBufferException;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;
import tv.twitchbot.common.dto.proto.messages.MessageOuterClass;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by naomi on 10/4/16.
 */
public abstract class Message {
    public static Message fromBytes(byte[] bytes) throws InvalidProtocolBufferException {
        return fromProto(MessageOuterClass.Message.parseFrom(bytes));
    }

    public static Message fromProto(MessageOuterClass.Message message) {
        Module module = Module.fromProto(message.getFrom());
        switch(message.getType()) {
            case EVENT: return Event.fromProto(module, message.getExtension(EventOuterClass.Event.data));
            case REQUEST: return Request.fromProto(module, message.getExtension(RequestOuterClass.Request.data));
            case RESPONSE: return Response.fromProto(module, message.getExtension(RequestOuterClass.Response.data));
            default: throw new IllegalStateException("Unknown message type " + message.getType().name());
        }
    }

    private Module from;

    public Message(Module from) {
        this.from = from;
    }

    public Module getFrom() {
        return from;
    }

    public MessageOuterClass.Message toProto() {
        return toProtoMessage().build();
    }

    protected MessageOuterClass.Message.Builder toProtoMessage() {
        return MessageOuterClass.Message.newBuilder()
                .setFrom(from.toProto());
    }

    public byte[] toBytes() {
        return toProto().toByteArray();
    }
}
