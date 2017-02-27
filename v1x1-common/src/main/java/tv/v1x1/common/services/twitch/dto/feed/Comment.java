package tv.v1x1.common.services.twitch.dto.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.users.User;

import java.util.List;
import java.util.Map;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {
    @JsonProperty
    private String body;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty
    private boolean deleted;
    @JsonProperty
    private List<Emote> emotes;
    @JsonProperty
    private String id;
    @JsonProperty
    private Permissions permissions;
    @JsonProperty
    private Map<String, Reaction> reactions;
    @JsonProperty
    private User user;

    public Comment() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }

    public List<Emote> getEmotes() {
        return emotes;
    }

    public void setEmotes(final List<Emote> emotes) {
        this.emotes = emotes;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(final Permissions permissions) {
        this.permissions = permissions;
    }

    public Map<String, Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(final Map<String, Reaction> reactions) {
        this.reactions = reactions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}
