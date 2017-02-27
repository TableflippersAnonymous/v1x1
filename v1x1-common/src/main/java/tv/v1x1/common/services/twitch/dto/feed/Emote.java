package tv.v1x1.common.services.twitch.dto.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Emote {
    @JsonProperty
    private long end;
    @JsonProperty
    private long id;
    @JsonProperty
    private long set;
    @JsonProperty
    private long start;

    public Emote() {
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(final long end) {
        this.end = end;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public long getSet() {
        return set;
    }

    public void setSet(final long set) {
        this.set = set;
    }

    public long getStart() {
        return start;
    }

    public void setStart(final long start) {
        this.start = start;
    }
}
