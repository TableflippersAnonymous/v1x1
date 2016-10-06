package tv.twitchbot.common.dto.messages.requests;

import tv.twitchbot.common.dto.core.Channel;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.messages.Request;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by naomi on 10/5/16.
 */
public class SendMessageRequest extends Request {
    public static SendMessageRequest fromProto(Module module, UUID uuid, long timestamp, String responseQueueName, RequestOuterClass.SendMessageRequest sendMessageRequest) {
        Channel destination = Channel.fromProto(sendMessageRequest.getDestination());
        String text = sendMessageRequest.getText();
        return new SendMessageRequest(module, uuid, timestamp, responseQueueName, destination, text);
    }

    private Channel destination;
    private String text;

    public SendMessageRequest(Module from, String responseQueueName, Channel destination, String text) {
        super(from, responseQueueName);
        this.destination = destination;
        this.text = text;
    }

    public SendMessageRequest(Module from, UUID messageId, long timestamp, String responseQueueName, Channel destination, String text) {
        super(from, messageId, timestamp, responseQueueName);
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
