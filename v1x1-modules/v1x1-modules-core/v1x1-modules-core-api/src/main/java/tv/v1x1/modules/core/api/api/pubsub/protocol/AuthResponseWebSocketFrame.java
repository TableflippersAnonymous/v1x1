package tv.v1x1.modules.core.api.api.pubsub.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by cobi on 7/9/2017.
 */
public class AuthResponseWebSocketFrame extends ResponseWebSocketFrame {
    @JsonProperty
    private UUID principal;

    public AuthResponseWebSocketFrame() {
        super();
    }

    public AuthResponseWebSocketFrame(final UUID id, final UUID responseTo, final UUID principal) {
        super(id, WebSocketFrameType.AUTH_RESPONSE, responseTo);
        this.principal = principal;
    }

    public UUID getPrincipal() {
        return principal;
    }

    public void setPrincipal(final UUID principal) {
        this.principal = principal;
    }
}
