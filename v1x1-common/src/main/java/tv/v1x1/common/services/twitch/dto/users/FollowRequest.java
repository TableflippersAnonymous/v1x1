package tv.v1x1.common.services.twitch.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 2/28/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowRequest {
    @JsonProperty
    private boolean notifications;

    public FollowRequest() {
    }

    public FollowRequest(final boolean notifications) {
        this.notifications = notifications;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(final boolean notifications) {
        this.notifications = notifications;
    }
}
