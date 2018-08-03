package tv.v1x1.common.services.discord.resources;

import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.oauth2.ApplicationInformation;
import tv.v1x1.common.services.discord.dto.oauth2.TokenResponse;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

/**
 * Created by cobi on 9/17/2017.
 */
public class OAuth2Resource {
    private final WebTarget oauth2;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public OAuth2Resource(final WebTarget oauth2, final String clientId, final String clientSecret, final String redirectUri) {
        this.oauth2 = oauth2;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public TokenResponse getToken(final String code, final String state) {
        return oauth2.path("token")
                .request(DiscordApi.ACCEPT)
                .post(Entity.form(new Form()
                        .param("client_id", clientId)
                        .param("client_secret", clientSecret)
                        .param("grant_type", "authorization_code")
                        .param("redirect_uri", redirectUri)
                        .param("code", code)
                        .param("state", state)), TokenResponse.class);
    }

    public TokenResponse refreshToken(final String refreshToken) {
        return oauth2.path("token")
                .request(DiscordApi.ACCEPT)
                .post(Entity.form(new Form()
                        .param("client_id", clientId)
                        .param("client_secret", clientSecret)
                        .param("grant_type", "refresh_token")
                        .param("refresh_token", refreshToken)
                        .param("redirect_uri", redirectUri)), TokenResponse.class);
    }

    public ApplicationInformation getCurrentApplicationInformation() {
        return oauth2.path("applications").path("@me")
                .request(DiscordApi.ACCEPT)
                .get(ApplicationInformation.class);
    }
}
