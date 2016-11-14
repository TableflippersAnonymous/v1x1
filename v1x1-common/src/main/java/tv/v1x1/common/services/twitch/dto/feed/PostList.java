package tv.v1x1.common.services.twitch.dto.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostList {
    @JsonProperty("_total")
    private long total;

    @JsonProperty("_cursor")
    private String cursor;

    @JsonProperty
    private List<Post> posts;

    public PostList() {
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(final String cursor) {
        this.cursor = cursor;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(final List<Post> posts) {
        this.posts = posts;
    }
}
