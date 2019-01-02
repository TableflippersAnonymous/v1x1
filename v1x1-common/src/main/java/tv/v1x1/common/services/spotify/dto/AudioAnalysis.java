package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioAnalysis {
    @JsonProperty
    private List<TimeInterval> bars;
    @JsonProperty
    private List<TimeInterval> beats;
    @JsonProperty
    private List<Section> sections;
    @JsonProperty
    private List<Segment> segments;
    @JsonProperty
    private List<TimeInterval> tatums;

    public AudioAnalysis() {
    }

    public AudioAnalysis(final List<TimeInterval> bars, final List<TimeInterval> beats, final List<Section> sections,
                         final List<Segment> segments, final List<TimeInterval> tatums) {
        this.bars = bars;
        this.beats = beats;
        this.sections = sections;
        this.segments = segments;
        this.tatums = tatums;
    }

    public List<TimeInterval> getBars() {
        return bars;
    }

    public void setBars(final List<TimeInterval> bars) {
        this.bars = bars;
    }

    public List<TimeInterval> getBeats() {
        return beats;
    }

    public void setBeats(final List<TimeInterval> beats) {
        this.beats = beats;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(final List<Section> sections) {
        this.sections = sections;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(final List<Segment> segments) {
        this.segments = segments;
    }

    public List<TimeInterval> getTatums() {
        return tatums;
    }

    public void setTatums(final List<TimeInterval> tatums) {
        this.tatums = tatums;
    }
}
