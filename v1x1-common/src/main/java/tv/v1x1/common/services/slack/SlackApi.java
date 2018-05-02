package tv.v1x1.common.services.slack;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class SlackApi {
    public static final String ACCEPT = "application/json";
    public static final String BASE_URL = "https://slack.com/api";

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String oauthToken;

    public SlackApi(final String clientId, final String clientSecret, final String redirectUri, final String oauthToken) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.oauthToken = oauthToken;
        final SlackApiRequestFilter slackApiRequestFilter = new SlackApiRequestFilter("Bearer " + oauthToken);
        final Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        client.register(slackApiRequestFilter);
        final WebTarget api = client.target(BASE_URL);
    }
}
