package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cursor {
    @JsonProperty
    private String after;

    public Cursor() {
    }

    public Cursor(final String after) {
        this.after = after;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(final String after) {
        this.after = after;
    }
}
