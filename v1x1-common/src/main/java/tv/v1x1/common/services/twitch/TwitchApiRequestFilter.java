package tv.v1x1.common.services.twitch;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

/**
 * @author Cobi
 */
public class TwitchApiRequestFilter implements ClientRequestFilter {
    private final String clientId;
    private final String oauthToken;

    public TwitchApiRequestFilter(final String clientId, final String oauthToken) {
        this.clientId = clientId;
        this.oauthToken = oauthToken;
    }

    @Override
    public void filter(final ClientRequestContext requestContext) {
        requestContext.getHeaders().add("Authorization", "OAuth " + oauthToken);
        requestContext.getHeaders().add("Client-ID", clientId);
    }
}
