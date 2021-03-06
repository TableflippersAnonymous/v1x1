package tv.v1x1.common.dto.messages.requests;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Request;
import tv.v1x1.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by cobi on 10/5/16.
 */
public class SendMessageRequest extends Request {
    private final Channel destination;
    private final String text;

    public static SendMessageRequest fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final String responseQueueName, final RequestOuterClass.SendMessageRequest sendMessageRequest) {
        final Channel destination = Channel.fromProto(sendMessageRequest.getDestination());
        final String text = sendMessageRequest.getText();
        return new SendMessageRequest(module, uuid, timestamp, context, responseQueueName, destination, text);
    }

    public SendMessageRequest(final Module from, final String responseQueueName, final Channel destination, final String text) {
        super(from, responseQueueName);
        this.destination = destination;
        this.text = text;
    }

    public SendMessageRequest(final Module from, final UUID messageId, final long timestamp, final Context context, final String responseQueueName, final Channel destination, final String text) {
        super(from, messageId, timestamp, context, responseQueueName);
        this.destination = destination;
        this.text = text;
    }

    public Channel getDestination() {
        return destination;
    }

    public String getText() {
        return text;
    }

    @Override
    protected RequestOuterClass.Request.Builder toProtoRequest() {
        return super.toProtoRequest()
                .setType(RequestOuterClass.RequestType.SEND_MESSAGE)
                .setExtension(RequestOuterClass.SendMessageRequest.data, toProtoSendMessageRequest().build());
    }

    protected RequestOuterClass.SendMessageRequest.Builder toProtoSendMessageRequest() {
        return RequestOuterClass.SendMessageRequest.newBuilder()
                .setDestination(destination.toProto())
                .setText(text);
    }
}
