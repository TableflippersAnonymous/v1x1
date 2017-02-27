package tv.v1x1.common.services.twitch.dto.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostList {
    @JsonProperty("_cursor")
    private String cursor;

    @JsonProperty("_topic")
    private String topic;

    @JsonProperty("_disabled")
    private boolean disabled;

    @JsonProperty
    private List<Post> posts;

    public PostList() {
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(final String cursor) {
        this.cursor = cursor;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(final String topic) {
        this.topic = topic;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(final List<Post> posts) {
        this.posts = posts;
    }
}
