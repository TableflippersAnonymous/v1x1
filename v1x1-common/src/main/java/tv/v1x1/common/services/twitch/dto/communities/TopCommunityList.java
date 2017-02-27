package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopCommunityList {
    @JsonProperty("_cursor")
    private String cursor;
    @JsonProperty("_total")
    private long total;
    @JsonProperty
    private List<TopCommunity> communities;

    public TopCommunityList() {
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(final String cursor) {
        this.cursor = cursor;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public List<TopCommunity> getCommunities() {
        return communities;
    }

    public void setCommunities(final List<TopCommunity> communities) {
        this.communities = communities;
    }
}
