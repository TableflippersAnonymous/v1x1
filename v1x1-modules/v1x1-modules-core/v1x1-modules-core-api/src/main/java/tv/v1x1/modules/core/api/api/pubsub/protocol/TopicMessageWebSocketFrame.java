package tv.v1x1.modules.core.api.api.pubsub.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by naomi on 7/9/2017.
 */
public class TopicMessageWebSocketFrame extends WebSocketFrame {
    @JsonProperty
    private String from;
    @JsonProperty("message_id")
    private UUID messageId;
    @JsonProperty
    private long timestamp;
    @JsonProperty
    private String topic;
    @JsonProperty
    private String payload;

    public TopicMessageWebSocketFrame() {
        super();
    }

    public TopicMessageWebSocketFrame(final UUID id, final String from, final UUID messageId, final long timestamp, final String topic, final String payload) {
        super(id, WebSocketFrameType.TOPIC_MESSAGE);
        this.from = from;
        this.messageId = messageId;
        this.timestamp = timestamp;
        this.topic = topic;
        this.payload = payload;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(final UUID messageId) {
        this.messageId = messageId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(final String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(final String payload) {
        this.payload = payload;
    }
}
