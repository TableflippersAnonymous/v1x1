package tv.twitchbot.common.dto.messages;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.messages.responses.ModuleShutdownResponse;
import tv.twitchbot.common.dto.messages.responses.SendMessageResponse;
import tv.twitchbot.common.dto.proto.messages.MessageOuterClass;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by cobi on 10/4/16.
 */
public abstract class Response<T extends Request> extends Message {
    public static Response<? extends Request> fromProto(Module module, UUID uuid, long timestamp, RequestOuterClass.Response response) {
        UUID requestMessageId = UUID.fromProto(response.getRequestMessageId());
        switch(response.getType()) {
            case MODULE_SHUTDOWN: return ModuleShutdownResponse.fromProto(module, uuid, timestamp, requestMessageId, response.getExtension(RequestOuterClass.ModuleShutdownResponse.data));
            case SEND_MESSAGE: return SendMessageResponse.fromProto(module, uuid, timestamp, requestMessageId, response.getExtension(RequestOuterClass.SendMessageResponse.data));
            default: throw new IllegalStateException("Unknown request type " + response.getType().name());
        }
    }

    private UUID requestMessageId;

    public Response(Module from, UUID requestMessageId) {
        super(from);
        this.requestMessageId = requestMessageId;
    }

    public Response(Module from, UUID messageId, long timestamp, UUID requestMessageId) {
        super(from, messageId, timestamp);
        this.requestMessageId = requestMessageId;
    }

    public UUID getRequestMessageId() {
        return requestMessageId;
    }

    @Override
    protected MessageOuterClass.Message.Builder toProtoMessage() {
        return super.toProtoMessage()
                .setType(MessageOuterClass.Message.MessageType.RESPONSE)
                .setExtension(RequestOuterClass.Response.data, toProtoResponse().build());
    }

    protected RequestOuterClass.Response.Builder toProtoResponse() {
        return RequestOuterClass.Response.newBuilder()
                .setRequestMessageId(requestMessageId.toProto());
    }
}
