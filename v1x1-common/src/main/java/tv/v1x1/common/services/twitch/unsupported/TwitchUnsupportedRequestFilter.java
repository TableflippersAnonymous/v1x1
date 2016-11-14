package tv.v1x1.common.services.twitch.unsupported;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

/**
 * Created by naomi on 11/13/2016.
 */
public class TwitchUnsupportedRequestFilter implements ClientRequestFilter {
    private final String clientId;

    public TwitchUnsupportedRequestFilter(final String clientId) {
        this.clientId = clientId;
    }

    @Override
    public void filter(final ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().add("Client-ID", clientId);
    }
}
