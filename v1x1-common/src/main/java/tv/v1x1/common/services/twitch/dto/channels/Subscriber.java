package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.users.User;

/**
 * Created by cobi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Subscriber {
    @JsonProperty("_id")
    private String id;
    @JsonProperty
    private User user;
    @JsonProperty("created_at")
    private String createdAt;

    public Subscriber() {
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }
}
