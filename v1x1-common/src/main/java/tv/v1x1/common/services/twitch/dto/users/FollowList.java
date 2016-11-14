package tv.v1x1.common.services.twitch.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowList {
    @JsonProperty("_total")
    private long total;
    @JsonProperty
    private List<Follow> follows;

    public FollowList() {
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public List<Follow> getFollows() {
        return follows;
    }

    public void setFollows(final List<Follow> follows) {
        this.follows = follows;
    }
}
