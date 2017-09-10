package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.user.User;

/**
 * Created by cobi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ban {
    @JsonProperty
    private String reason;
    @JsonProperty
    private User user;

    public Ban() {
    }

    public Ban(final String reason, final User user) {
        this.reason = reason;
        this.user = user;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}
