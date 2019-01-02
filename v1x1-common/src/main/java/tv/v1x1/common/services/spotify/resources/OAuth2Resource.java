package tv.v1x1.common.services.spotify.resources;

import tv.v1x1.common.services.spotify.SpotifyApi;
import tv.v1x1.common.services.spotify.dto.AuthorizationResponse;
import tv.v1x1.common.services.spotify.dto.RefreshResponse;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

public class OAuth2Resource {
    private final WebTarget token;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public OAuth2Resource(final WebTarget token, final String clientId, final String clientSecret, final String redirectUri) {
        this.token = token;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public AuthorizationResponse authorize(final String code) {
        return token.request(SpotifyApi.ACCEPT)
                .post(Entity.form(new Form()
                        .param("grant_type", "authorization_code")
                        .param("code", code)
                        .param("redirect_uri", redirectUri)
                        .param("client_id", clientId)
                        .param("client_secret", clientSecret)
                ), AuthorizationResponse.class);
    }

    public RefreshResponse refreshToken(final String refreshToken) {
        return token.request(SpotifyApi.ACCEPT)
                .post(Entity.form(new Form()
                        .param("grant_type", "refresh_token")
                        .param("refresh_token", refreshToken)
                        .param("client_id", clientId)
                        .param("client_secret", clientSecret)
                ), RefreshResponse.class);
    }
}
