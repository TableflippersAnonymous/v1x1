package tv.v1x1.common.services.twitch.dto.games;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopGameList {
    @JsonProperty("_total")
    private long total;
    @JsonProperty
    private List<TopGame> top;

    public TopGameList() {
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public List<TopGame> getTop() {
        return top;
    }

    public void setTop(final List<TopGame> top) {
        this.top = top;
    }
}
