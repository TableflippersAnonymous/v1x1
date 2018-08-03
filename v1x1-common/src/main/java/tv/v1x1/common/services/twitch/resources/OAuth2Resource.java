package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.auth.TokenResponse;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

/**
 * Created by naomi on 12/15/2016.
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

    /**
     * Get an OAuth2 token from an OAuth2 code.
     * @param code OAuth2 code from Twitch's OAuth2 flow
     * @param state State data used to generate the code
     */
    public TokenResponse getToken(final String code, final String state) {
        return oauth2
                .path("token")
                .request(TwitchApi.ACCEPT)
                .post(Entity.form(new Form()
                        .param("client_id", clientId)
                        .param("client_secret", clientSecret)
                        .param("grant_type", "authorization_code")
                        .param("redirect_uri", redirectUri)
                        .param("code", code)
                        .param("state", state)), TokenResponse.class);
    }
}
