package tv.v1x1.common.dto.messages;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.responses.ExceptionResponse;
import tv.v1x1.common.dto.messages.responses.ModuleShutdownResponse;
import tv.v1x1.common.dto.messages.responses.ScheduleResponse;
import tv.v1x1.common.dto.messages.responses.SendMessageResponse;
import tv.v1x1.common.dto.proto.messages.MessageOuterClass;
import tv.v1x1.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by cobi on 10/4/16.
 */
public abstract class Response<T extends Request> extends Message {
    public static Response<? extends Request> fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final RequestOuterClass.Response response) {
        final UUID requestMessageId = UUID.fromProto(response.getRequestMessageId());
        switch(response.getType()) {
            case MODULE_SHUTDOWN: return ModuleShutdownResponse.fromProto(module, uuid, timestamp, context, requestMessageId, response.getExtension(RequestOuterClass.ModuleShutdownResponse.data));
            case SEND_MESSAGE: return SendMessageResponse.fromProto(module, uuid, timestamp, context, requestMessageId, response.getExtension(RequestOuterClass.SendMessageResponse.data));
            case SCHEDULE: return ScheduleResponse.fromProto(module, uuid, timestamp, context, requestMessageId, response.getExtension(RequestOuterClass.ScheduleResponse.data));
            case EXCEPTION: return ExceptionResponse.fromProto(module, uuid, timestamp, context, requestMessageId, response.getExtension(RequestOuterClass.ExceptionResponse.data));
            default: throw new IllegalStateException("Unknown request type " + response.getType().name());
        }
    }

    private final UUID requestMessageId;

    public Response(final Module from, final UUID requestMessageId) {
        super(from);
        this.requestMessageId = requestMessageId;
    }

    public Response(final Module from, final UUID messageId, final long timestamp, final Context context, final UUID requestMessageId) {
        super(from, messageId, timestamp, context);
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
