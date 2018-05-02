package tv.v1x1.common.services.slack;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

public class SlackApiRequestFilter implements ClientRequestFilter {
    private final String authorization;

    public SlackApiRequestFilter(final String authorization) {
        this.authorization = authorization;
    }

    @Override
    public void filter(final ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().add("Authorization", authorization);
        requestContext.getHeaders().add("User-Agent", "v1x1 (https://v1x1.tv, 1)");
    }
}
