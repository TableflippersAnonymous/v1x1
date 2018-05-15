package tv.v1x1.common.services.twitch.dto.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.users.User;

import java.util.List;
import java.util.Map;

/**
 * Created by cobi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    @JsonProperty
    private String id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty
    private boolean deleted;
    @JsonProperty
    private List<String> emotes;
    @JsonProperty
    private Map<String, Reaction> reactions;
    @JsonProperty
    private String body;
    @JsonProperty
    private User user;
    @JsonProperty
    private CommentList comments;
    @JsonProperty
    private Permissions permissions;

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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }

    public List<String> getEmotes() {
        return emotes;
    }

    public void setEmotes(final List<String> emotes) {
        this.emotes = emotes;
    }

    public Map<String, Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(final Map<String, Reaction> reactions) {
        this.reactions = reactions;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public CommentList getComments() {
        return comments;
    }

    public void setComments(final CommentList comments) {
        this.comments = comments;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(final Permissions permissions) {
        this.permissions = permissions;
    }
}
