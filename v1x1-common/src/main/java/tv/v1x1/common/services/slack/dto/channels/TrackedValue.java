package tv.v1x1.common.services.slack.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrackedValue {
    @JsonProperty
    private String value;
    @JsonProperty
    private String creator;
    @JsonProperty("last_set")
    private long lastSet;

    public TrackedValue() {
    }

    public TrackedValue(final String value, final String creator, final long lastSet) {
        this.value = value;
        this.creator = creator;
        this.lastSet = lastSet;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(final String creator) {
        this.creator = creator;
    }

    public long getLastSet() {
        return lastSet;
    }

    public void setLastSet(final long lastSet) {
        this.lastSet = lastSet;
    }
}
