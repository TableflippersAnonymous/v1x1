package tv.v1x1.modules.core.api.api.pubsub.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by naomi on 7/9/2017.
 */
public class AuthRequestWebSocketFrame extends WebSocketFrame {
    @JsonProperty
    private String authorization;

    public AuthRequestWebSocketFrame() {
        super();
    }

    public AuthRequestWebSocketFrame(final UUID id, final String authorization) {
        super(id, WebSocketFrameType.AUTH_REQUEST);
        this.authorization = authorization;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(final String authorization) {
        this.authorization = authorization;
    }
}
