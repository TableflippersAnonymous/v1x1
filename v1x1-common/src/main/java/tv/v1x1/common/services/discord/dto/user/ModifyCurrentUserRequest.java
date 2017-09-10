package tv.v1x1.common.services.discord.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyCurrentUserRequest {
    @JsonProperty
    private String username;
    @JsonProperty
    private String avatar;

    public ModifyCurrentUserRequest() {
    }

    public ModifyCurrentUserRequest(final String username) {
        this.username = username;
    }

    public ModifyCurrentUserRequest(final String username, final String avatar) {
        this.username = username;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }
}
