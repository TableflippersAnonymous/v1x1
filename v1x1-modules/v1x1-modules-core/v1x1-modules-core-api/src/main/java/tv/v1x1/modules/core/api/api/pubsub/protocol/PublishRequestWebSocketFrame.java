package tv.v1x1.modules.core.api.api.pubsub.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by cobi on 7/9/2017.
 */
public class PublishRequestWebSocketFrame extends WebSocketFrame {
    @JsonProperty
    private String topic;
    @JsonProperty
    private String payload;

    public PublishRequestWebSocketFrame() {
        super();
    }

    public PublishRequestWebSocketFrame(final UUID id, final String topic, final String payload) {
        super(id, WebSocketFrameType.PUBLISH_REQUEST);
        this.topic = topic;
        this.payload = payload;
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
