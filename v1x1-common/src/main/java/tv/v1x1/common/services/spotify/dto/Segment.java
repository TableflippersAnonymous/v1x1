package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Segment {
    @JsonProperty
    private float start;
    @JsonProperty
    private float duration;
    @JsonProperty
    private float confidence;
    @JsonProperty("loudness_start")
    private float loudnessStart;
    @JsonProperty("loudness_max")
    private float loudnessMax;
    @JsonProperty("loudness_max_time")
    private float loudnessMaxTime;
    @JsonProperty("loudness_end")
    private float loudnessEnd;
    @JsonProperty
    private List<Float> pitches;
    @JsonProperty
    private List<Float> timbre;

    public Segment() {
    }

    public Segment(final float start, final float duration, final float confidence, final float loudnessStart,
                   final float loudnessMax, final float loudnessMaxTime, final float loudnessEnd,
                   final List<Float> pitches, final List<Float> timbre) {
        this.start = start;
        this.duration = duration;
        this.confidence = confidence;
        this.loudnessStart = loudnessStart;
        this.loudnessMax = loudnessMax;
        this.loudnessMaxTime = loudnessMaxTime;
        this.loudnessEnd = loudnessEnd;
        this.pitches = pitches;
        this.timbre = timbre;
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

    public float getLoudnessStart() {
        return loudnessStart;
    }

    public void setLoudnessStart(final float loudnessStart) {
        this.loudnessStart = loudnessStart;
    }

    public float getLoudnessMax() {
        return loudnessMax;
    }

    public void setLoudnessMax(final float loudnessMax) {
        this.loudnessMax = loudnessMax;
    }

    public float getLoudnessMaxTime() {
        return loudnessMaxTime;
    }

    public void setLoudnessMaxTime(final float loudnessMaxTime) {
        this.loudnessMaxTime = loudnessMaxTime;
    }

    public float getLoudnessEnd() {
        return loudnessEnd;
    }

    public void setLoudnessEnd(final float loudnessEnd) {
        this.loudnessEnd = loudnessEnd;
    }

    public List<Float> getPitches() {
        return pitches;
    }

    public void setPitches(final List<Float> pitches) {
        this.pitches = pitches;
    }

    public List<Float> getTimbre() {
        return timbre;
    }

    public void setTimbre(final List<Float> timbre) {
        this.timbre = timbre;
    }
}
