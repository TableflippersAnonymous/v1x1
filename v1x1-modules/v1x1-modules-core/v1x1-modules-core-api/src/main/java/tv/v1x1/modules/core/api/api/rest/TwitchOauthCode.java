package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 12/17/2016.
 */
public class TwitchOauthCode {
    @JsonProperty("oauth_code")
    private String oauthCode;

    @JsonProperty("oauth_state")
    private String oauthState;

    public TwitchOauthCode() {
    }

    public TwitchOauthCode(final String oauthCode, final String oauthState) {
        this.oauthCode = oauthCode;
        this.oauthState = oauthState;
    }

    public String getOauthCode() {
        return oauthCode;
    }

    public void setOauthCode(final String oauthCode) {
        this.oauthCode = oauthCode;
    }

    public String getOauthState() {
        return oauthState;
    }

    public void setOauthState(final String oauthState) {
        this.oauthState = oauthState;
    }
}
