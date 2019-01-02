package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SavedTrack {
    @JsonProperty("added_at")
    private String addedAt;
    @JsonProperty
    private Track track;

    public SavedTrack() {
    }

    public SavedTrack(final String addedAt, final Track track) {
        this.addedAt = addedAt;
        this.track = track;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(final String addedAt) {
        this.addedAt = addedAt;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(final Track track) {
        this.track = track;
    }
}
