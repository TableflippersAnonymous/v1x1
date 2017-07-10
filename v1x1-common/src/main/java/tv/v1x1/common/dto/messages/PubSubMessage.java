package tv.v1x1.common.dto.messages;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.proto.messages.MessageOuterClass;
import tv.v1x1.common.dto.proto.messages.PubSub;

/**
 * Created by cobi on 5/26/2017.
 */
public class PubSubMessage extends Message {
    public static PubSubMessage fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final PubSub.PubSubMessage pubSubMessage) {
        final String topic = pubSubMessage.getTopic();
        final String json = pubSubMessage.getJson();
        return new PubSubMessage(module, uuid, timestamp, context, topic, json);
    }

    private String topic;
    private String json;

    public PubSubMessage(final Module from, final String topic, final String json) {
        super(from);
        this.topic = topic;
        this.json = json;
    }

    public PubSubMessage(final Module from, final UUID messageId, final long timestamp, final Context context, final String topic, final String json) {
        super(from, messageId, timestamp, context);
        this.topic = topic;
        this.json = json;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(final String topic) {
        this.topic = topic;
    }

    public String getJson() {
        return json;
    }

    public void setJson(final String json) {
        this.json = json;
    }

    protected PubSub.PubSubMessage.Builder toProtoPubSub() {
        return PubSub.PubSubMessage.newBuilder()
                .setTopic(topic)
                .setJson(json);
    }

    @Override
    protected MessageOuterClass.Message.Builder toProtoMessage() {
        return super.toProtoMessage()
                .setType(MessageOuterClass.Message.MessageType.PUBSUB)
                .setExtension(PubSub.PubSubMessage.data, toProtoPubSub().build());
    }
}
