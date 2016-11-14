package tv.v1x1.common.services.twitch.dto.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reaction {
    @JsonProperty
    private long count;
    @JsonProperty("user_ids")
    private List<Long> userIds;

    public Reaction() {
    }

    public long getCount() {
        return count;
    }

    public void setCount(final long count) {
        this.count = count;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(final List<Long> userIds) {
        this.userIds = userIds;
    }
}
