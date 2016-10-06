package tv.twitchbot.common.dto.messages.responses;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.messages.Response;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by naomi on 10/5/16.
 */
public class SendMessageResponse extends Response {
    public static SendMessageResponse fromProto(Module module, UUID uuid, long timestamp, RequestOuterClass.SendMessageResponse extension) {
        return new SendMessageResponse(module, uuid, timestamp);
    }

    public SendMessageResponse(Module from) {
        super(from);
    }

    public SendMessageResponse(Module from, UUID messageId, long timestamp) {
        super(from, messageId, timestamp);
    }

    @Override
    protected RequestOuterClass.Response.Builder toProtoResponse() {
        return super.toProtoResponse()
                .setType(RequestOuterClass.RequestType.SEND_MESSAGE)
                .setExtension(RequestOuterClass.SendMessageResponse.data, RequestOuterClass.SendMessageResponse.newBuilder().build());
    }
}
