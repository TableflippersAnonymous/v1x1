package tv.v1x1.common.services.twitch.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.channels.Channel;

/**
 * Created by naomi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Follow {
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty
    private boolean notifications;
    @JsonProperty
    private Channel channel;

    public Follow() {
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(final boolean notifications) {
        this.notifications = notifications;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(final Channel channel) {
        this.channel = channel;
    }
}
