package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Recommendations {
    @JsonProperty
    private List<RecommendationSeed> seeds;
    @JsonProperty
    private List<SimpleTrack> tracks;

    public Recommendations() {
    }

    public Recommendations(final List<RecommendationSeed> seeds, final List<SimpleTrack> tracks) {
        this.seeds = seeds;
        this.tracks = tracks;
    }

    public List<RecommendationSeed> getSeeds() {
        return seeds;
    }

    public void setSeeds(final List<RecommendationSeed> seeds) {
        this.seeds = seeds;
    }

    public List<SimpleTrack> getTracks() {
        return tracks;
    }

    public void setTracks(final List<SimpleTrack> tracks) {
        this.tracks = tracks;
    }
}
