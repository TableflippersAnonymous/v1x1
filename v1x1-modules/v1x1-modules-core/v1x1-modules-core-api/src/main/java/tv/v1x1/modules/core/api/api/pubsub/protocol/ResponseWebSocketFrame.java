package tv.v1x1.modules.core.api.api.pubsub.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by cobi on 7/9/2017.
 */
public abstract class ResponseWebSocketFrame extends WebSocketFrame {
    @JsonProperty("response_to")
    private UUID responseTo;

    public ResponseWebSocketFrame() {
        super();
    }

    public ResponseWebSocketFrame(final UUID id, final WebSocketFrameType type, final UUID responseTo) {
        super(id, type);
        this.responseTo = responseTo;
    }

    public UUID getResponseTo() {
        return responseTo;
    }

    public void setResponseTo(final UUID responseTo) {
        this.responseTo = responseTo;
    }
}
