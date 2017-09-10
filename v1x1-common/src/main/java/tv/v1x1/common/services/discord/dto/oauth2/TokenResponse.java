package tv.v1x1.common.services.discord.dto.oauth2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.guild.Guild;

/**
 * Created by cobi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty
    private String scope;
    @JsonProperty("expires_in")
    private int expiresIn;
    @JsonProperty("refresh_tokens")
    private String refreshToken;
    @JsonProperty
    private Guild guild;

    public TokenResponse() {
    }

    public TokenResponse(final String tokenType, final String accessToken, final String scope, final int expiresIn,
                         final String refreshToken, final Guild guild) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.scope = scope;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.guild = guild;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(final String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(final String scope) {
        this.scope = scope;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(final int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(final Guild guild) {
        this.guild = guild;
    }
}
