package tv.v1x1.common.services.discord;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

/**
 * Created by cobi on 9/10/2017.
 */
public class DiscordApiRequestFilter implements ClientRequestFilter {
    private final String authorization;

    public DiscordApiRequestFilter(final String authorization) {
        this.authorization = authorization;
    }

    @Override
    public void filter(final ClientRequestContext requestContext) {
        requestContext.getHeaders().add("Authorization", authorization);
        requestContext.getHeaders().add("User-Agent", "v1x1 (https://v1x1.tv, 1)");
    }
}
