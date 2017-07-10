package tv.v1x1.modules.core.api.api.pubsub.protocol;

import java.util.UUID;

/**
 * Created by cobi on 7/9/2017.
 */
public class HelloWebSocketFrame extends WebSocketFrame {
    public HelloWebSocketFrame() {
        super();
    }

    public HelloWebSocketFrame(final UUID id) {
        super(id, WebSocketFrameType.HELLO);
    }
}
