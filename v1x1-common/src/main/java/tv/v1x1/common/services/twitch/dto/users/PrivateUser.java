package tv.v1x1.common.services.twitch.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrivateUser extends User {
    @JsonProperty
    private String email;
    @JsonProperty("email_verified")
    private boolean emailVerified;
    @JsonProperty
    private NotificationSettings notifications;
    @JsonProperty
    private boolean partnered;
    @JsonProperty("twitter_connected")
    private boolean twitterConnected;

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

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(final boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isTwitterConnected() {
        return twitterConnected;
    }

    public void setTwitterConnected(final boolean twitterConnected) {
        this.twitterConnected = twitterConnected;
    }
}
