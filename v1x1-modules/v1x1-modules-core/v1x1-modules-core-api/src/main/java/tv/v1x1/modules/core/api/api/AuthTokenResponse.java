package tv.v1x1.modules.core.api.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 12/17/2016.
 */
public class AuthTokenResponse {
    @JsonProperty
    private String authorization;

    @JsonProperty
    private long expires;

    public AuthTokenResponse() {
    }

    public AuthTokenResponse(final String authorization, final long expires) {
        this.authorization = authorization;
        this.expires = expires;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(final String authorization) {
        this.authorization = authorization;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(final long expires) {
        this.expires = expires;
    }
}
