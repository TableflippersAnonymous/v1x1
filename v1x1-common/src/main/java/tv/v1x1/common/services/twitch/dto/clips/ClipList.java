package tv.v1x1.common.services.twitch.dto.clips;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 2/28/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClipList {
    @JsonProperty("_cursor")
    private String cursor;
    @JsonProperty
    private List<Clip> clips;

    public String getCursor() {
        return cursor;
    }

    public void setCursor(final String cursor) {
        this.cursor = cursor;
    }

    public List<Clip> getClips() {
        return clips;
    }

    public void setClips(final List<Clip> clips) {
        this.clips = clips;
    }
}
