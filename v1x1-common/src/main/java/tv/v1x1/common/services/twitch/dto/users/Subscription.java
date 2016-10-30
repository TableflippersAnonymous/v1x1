package tv.v1x1.common.services.twitch.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.channels.Channel;

/**
 * Created by naomi on 10/30/2016.
 */
public class Subscription {
    @JsonProperty
    private Channel channel;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("created_at")
    private String createdAt;

    public Subscription() {
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(final Channel channel) {
        this.channel = channel;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }
}
