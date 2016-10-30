package tv.v1x1.common.services.twitch.dto.blocks;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.users.User;

/**
 * Created by naomi on 10/28/2016.
 */
public class Block {
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty
    private User user;
    @JsonProperty("_id")
    private long id;

    public Block() {
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }
}
