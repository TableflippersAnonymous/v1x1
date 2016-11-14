package tv.v1x1.common.services.twitch.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationSettings {
    @JsonProperty
    private boolean email;
    @JsonProperty
    private boolean push;

    public NotificationSettings() {
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(final boolean email) {
        this.email = email;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(final boolean push) {
        this.push = push;
    }
}
