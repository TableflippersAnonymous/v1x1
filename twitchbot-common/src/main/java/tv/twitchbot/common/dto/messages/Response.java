package tv.twitchbot.common.dto.messages;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.messages.responses.ModuleShutdownResponse;
import tv.twitchbot.common.dto.messages.responses.SendMessageResponse;
import tv.twitchbot.common.dto.proto.messages.MessageOuterClass;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by naomi on 10/4/16.
 */
public abstract class Response extends Message {
    public static Response fromProto(Module module, UUID uuid, long timestamp, RequestOuterClass.Response response) {
        switch(response.getType()) {
            case MODULE_SHUTDOWN: return ModuleShutdownResponse.fromProto(module, uuid, timestamp, response.getExtension(RequestOuterClass.ModuleShutdownResponse.data));
            case SEND_MESSAGE: return SendMessageResponse.fromProto(module, uuid, timestamp, response.getExtension(RequestOuterClass.SendMessageResponse.data));
            default: throw new IllegalStateException("Unknown request type " + response.getType().name());
        }
    }

    public Response(Module from) {
        super(from);
    }

    public Response(Module from, UUID messageId, long timestamp) {
        super(from, messageId, timestamp);
    }

    @Override
    protected MessageOuterClass.Message.Builder toProtoMessage() {
        return super.toProtoMessage()
                .setType(MessageOuterClass.Message.MessageType.RESPONSE)
                .setExtension(RequestOuterClass.Response.data, toProtoResponse().build());
    }

    protected RequestOuterClass.Response.Builder toProtoResponse() {
        return RequestOuterClass.Response.newBuilder();
    }
}
