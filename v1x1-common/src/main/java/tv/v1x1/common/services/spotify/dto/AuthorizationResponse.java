package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationResponse extends RefreshResponse {
    @JsonProperty("refresh_token")
    private String refreshToken;

    public AuthorizationResponse() {
    }

    public AuthorizationResponse(final String accessToken, final String tokenType, final String scope,
                                 final int expiresIn, final String refreshToken) {
        super(accessToken, tokenType, scope, expiresIn);
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
