package tv.v1x1.common.services.twitch.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RootResponse {
    @JsonProperty
    private Token token;

    public RootResponse() {
    }

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }
}
