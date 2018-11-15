package tv.v1x1.common.services.spotify;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

public class SpotifyApiRequestFilter implements ClientRequestFilter {
    private final String oauthToken;

    public SpotifyApiRequestFilter(final String oauthToken) {
        this.oauthToken = oauthToken;
    }

    @Override
    public void filter(final ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().add("Authorization", "Bearer " + oauthToken);
    }
}
