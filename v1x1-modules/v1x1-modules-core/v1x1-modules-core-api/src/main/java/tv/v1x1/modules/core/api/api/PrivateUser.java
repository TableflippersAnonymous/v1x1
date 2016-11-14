package tv.v1x1.modules.core.api.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.dto.db.Platform;

import java.util.UUID;

/**
 * Created by naomi on 11/13/2016.
 */
public class PrivateUser extends User {
    @JsonProperty("oauth_code")
    private String oauthCode;

    public PrivateUser() {
    }

    public PrivateUser(final UUID globalUserId, final Platform platform, final String userId, final String displayName, final String oauthCode) {
        super(globalUserId, platform, userId, displayName);
        this.oauthCode = oauthCode;
    }

    public String getOauthCode() {
        return oauthCode;
    }

    public void setOauthCode(final String oauthCode) {
        this.oauthCode = oauthCode;
    }
}
