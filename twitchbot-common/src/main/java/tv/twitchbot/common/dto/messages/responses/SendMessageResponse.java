package tv.twitchbot.common.dto.messages.responses;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.messages.Response;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by naomi on 10/5/16.
 */
public class SendMessageResponse extends Response {
    public static SendMessageResponse fromProto(Module module, UUID uuid, long timestamp, UUID requestMessageId, RequestOuterClass.SendMessageResponse extension) {
        return new SendMessageResponse(module, uuid, timestamp, requestMessageId);
    }

    public SendMessageResponse(Module from, UUID requestMessageId) {
        super(from, requestMessageId);
    }

    public SendMessageResponse(Module from, UUID messageId, long timestamp, UUID requestMessageId) {
        super(from, messageId, timestamp, requestMessageId);
    }

    @Override
    protected RequestOuterClass.Response.Builder toProtoResponse() {
        return super.toProtoResponse()
                .setType(RequestOuterClass.RequestType.SEND_MESSAGE)
                .setExtension(RequestOuterClass.SendMessageResponse.data, RequestOuterClass.SendMessageResponse.newBuilder().build());
    }
}
