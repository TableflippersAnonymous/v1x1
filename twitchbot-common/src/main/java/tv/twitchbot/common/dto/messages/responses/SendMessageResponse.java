package tv.twitchbot.common.dto.messages.responses;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.messages.Response;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by cobi on 10/5/16.
 */
public class SendMessageResponse extends Response {
    public static SendMessageResponse fromProto(Module module, RequestOuterClass.SendMessageResponse extension) {
        return new SendMessageResponse(module);
    }

    public SendMessageResponse(Module module) {
        super(module);
    }

    @Override
    protected RequestOuterClass.Response.Builder toProtoResponse() {
        return super.toProtoResponse()
                .setType(RequestOuterClass.RequestType.SEND_MESSAGE)
                .setExtension(RequestOuterClass.SendMessageResponse.data, RequestOuterClass.SendMessageResponse.newBuilder().build());
    }
}
