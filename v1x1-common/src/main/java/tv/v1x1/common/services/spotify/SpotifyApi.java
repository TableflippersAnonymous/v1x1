package tv.v1x1.common.services.spotify;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class SpotifyApi {
    public static final String ACCEPT = "application/json";
    public static final String BASE_URL = "https://api.spotify.com/v1";

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public SpotifyApi(final String clientId, final String oauthToken, final String clientSecret, final String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        final SpotifyApiRequestFilter spotifyApiRequestFilter = new SpotifyApiRequestFilter(oauthToken);
        final Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        client.register(spotifyApiRequestFilter);
    }
}
