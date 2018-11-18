package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeInterval {
    @JsonProperty
    private float start;
    @JsonProperty
    private float duration;
    @JsonProperty
    private float confidence;

    public TimeInterval() {
    }

    public TimeInterval(final float start, final float duration, final float confidence) {
        this.start = start;
        this.duration = duration;
        this.confidence = confidence;
    }

    public float getStart() {
        return start;
    }

    public void setStart(final float start) {
        this.start = start;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(final float duration) {
        this.duration = duration;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(final float confidence) {
        this.confidence = confidence;
    }
}
