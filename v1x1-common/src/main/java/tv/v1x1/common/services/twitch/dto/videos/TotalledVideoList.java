package tv.v1x1.common.services.twitch.dto.videos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/30/2016.
 */
public class TotalledVideoList extends VideoList {
    @JsonProperty("_total")
    private long total;

    public TotalledVideoList() {
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }
}
