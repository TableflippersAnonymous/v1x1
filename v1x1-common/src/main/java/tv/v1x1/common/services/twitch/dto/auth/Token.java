package tv.v1x1.common.services.twitch.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Token {
    @JsonProperty
    private Authorization authorization;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty
    private boolean valid;

    public Token() {
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(final Authorization authorization) {
        this.authorization = authorization;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(final boolean valid) {
        this.valid = valid;
    }
}
