package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowerList {
    @JsonProperty("_total")
    private long total;
    @JsonProperty("_cursor")
    private String cursor;
    @JsonProperty
    private List<Follower> follows;

    public FollowerList() {
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

    public List<Follower> getFollows() {
        return follows;
    }

    public void setFollows(final List<Follower> follows) {
        this.follows = follows;
    }
}
