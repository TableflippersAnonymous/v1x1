package tv.v1x1.common.dto.messages.responses;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Response;
import tv.v1x1.common.dto.messages.requests.SendMessageRequest;
import tv.v1x1.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by cobi on 10/5/16.
 */
public class SendMessageResponse extends Response<SendMessageRequest> {
    public static SendMessageResponse fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final UUID requestMessageId, final RequestOuterClass.SendMessageResponse extension) {
        return new SendMessageResponse(module, uuid, timestamp, context, requestMessageId);
    }

    public SendMessageResponse(final Module from, final UUID requestMessageId) {
        super(from, requestMessageId);
    }

    public SendMessageResponse(final Module from, final UUID messageId, final long timestamp, final Context context, final UUID requestMessageId) {
        super(from, messageId, timestamp, context, requestMessageId);
    }

    @Override
    protected RequestOuterClass.Response.Builder toProtoResponse() {
        return super.toProtoResponse()
                .setType(RequestOuterClass.RequestType.SEND_MESSAGE)
                .setExtension(RequestOuterClass.SendMessageResponse.data, RequestOuterClass.SendMessageResponse.newBuilder().build());
    }
}
