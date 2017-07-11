package tv.v1x1.modules.core.api.api.pubsub.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

/**
 * Created by cobi on 7/9/2017.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.EXISTING_PROPERTY,
              property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HelloWebSocketFrame.class, name = "HELLO"),
        @JsonSubTypes.Type(value = ErrorWebSocketFrame.class, name = "ERROR"),
        @JsonSubTypes.Type(value = AuthRequestWebSocketFrame.class, name = "AUTH_REQUEST"),
        @JsonSubTypes.Type(value = AuthResponseWebSocketFrame.class, name = "AUTH_RESPONSE"),
        @JsonSubTypes.Type(value = ListenRequestWebSocketFrame.class, name = "LISTEN_REQUEST"),
        @JsonSubTypes.Type(value = ListenResponseWebSocketFrame.class, name = "LISTEN_RESPONSE"),
        @JsonSubTypes.Type(value = UnlistenRequestWebSocketFrame.class, name = "UNLISTEN_REQUEST"),
        @JsonSubTypes.Type(value = UnlistenResponseWebSocketFrame.class, name = "UNLISTEN_RESPONSE"),
        @JsonSubTypes.Type(value = PublishRequestWebSocketFrame.class, name = "PUBLISH_REQUEST"),
        @JsonSubTypes.Type(value = PublishResponseWebSocketFrame.class, name = "PUBLISH_RESPONSE"),
        @JsonSubTypes.Type(value = TopicMessageWebSocketFrame.class, name = "TOPIC_MESSAGE")
})
public abstract class WebSocketFrame {
    public enum WebSocketFrameType {
        HELLO, ERROR,
        AUTH_REQUEST, AUTH_RESPONSE,
        LISTEN_REQUEST, LISTEN_RESPONSE,
        UNLISTEN_REQUEST, UNLISTEN_RESPONSE,
        PUBLISH_REQUEST, PUBLISH_RESPONSE,
        TOPIC_MESSAGE
    }

    @JsonProperty
    private UUID id;
    @JsonProperty
    private WebSocketFrameType type;

    public WebSocketFrame() {
    }

    public WebSocketFrame(final UUID id, final WebSocketFrameType type) {
        this.id = id;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public WebSocketFrameType getType() {
        return type;
    }

    public void setType(final WebSocketFrameType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WebSocketFrame{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }
}
