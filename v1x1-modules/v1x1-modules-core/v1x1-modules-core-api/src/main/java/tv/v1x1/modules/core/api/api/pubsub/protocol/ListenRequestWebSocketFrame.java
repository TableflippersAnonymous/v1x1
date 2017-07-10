package tv.v1x1.modules.core.api.api.pubsub.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by cobi on 7/9/2017.
 */
public class ListenRequestWebSocketFrame extends WebSocketFrame {
    @JsonProperty
    private String topic;

    public ListenRequestWebSocketFrame() {
        super();
    }

    public ListenRequestWebSocketFrame(final UUID id, final String topic) {
        super(id, WebSocketFrameType.LISTEN_REQUEST);
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(final String topic) {
        this.topic = topic;
    }
}
