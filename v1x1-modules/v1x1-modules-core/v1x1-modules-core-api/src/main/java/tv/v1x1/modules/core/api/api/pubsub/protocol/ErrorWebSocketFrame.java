package tv.v1x1.modules.core.api.api.pubsub.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by naomi on 7/9/2017.
 */
public class ErrorWebSocketFrame extends ResponseWebSocketFrame {
    @JsonProperty("error_type")
    private String errorType;
    @JsonProperty("error_message")
    private String errorMessage;

    public ErrorWebSocketFrame() {
        super();
    }

    public ErrorWebSocketFrame(final UUID id, final UUID responseTo, final String errorType, final String errorMessage) {
        super(id, WebSocketFrameType.ERROR, responseTo);
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(final String errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
