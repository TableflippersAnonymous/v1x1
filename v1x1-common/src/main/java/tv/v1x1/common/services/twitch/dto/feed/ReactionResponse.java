package tv.v1x1.common.services.twitch.dto.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.users.User;

/**
 * Created by cobi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReactionResponse {
    @JsonProperty
    private String id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("emote_id")
    private String emoteId;
    @JsonProperty
    private User user;

    public ReactionResponse() {
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

    public String getEmoteId() {
        return emoteId;
    }

    public void setEmoteId(final String emoteId) {
        this.emoteId = emoteId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}
