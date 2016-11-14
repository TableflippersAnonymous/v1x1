package tv.v1x1.common.services.twitch.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrivateUser extends User {
    @JsonProperty
    private String email;
    @JsonProperty
    private boolean partnered;
    @JsonProperty
    private NotificationSettings notifications;

    public PrivateUser() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public boolean isPartnered() {
        return partnered;
    }

    public void setPartnered(final boolean partnered) {
        this.partnered = partnered;
    }

    public NotificationSettings getNotifications() {
        return notifications;
    }

    public void setNotifications(final NotificationSettings notifications) {
        this.notifications = notifications;
    }
}
