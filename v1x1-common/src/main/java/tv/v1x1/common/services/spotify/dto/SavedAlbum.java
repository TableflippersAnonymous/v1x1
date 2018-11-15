package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SavedAlbum {
    @JsonProperty("added_at")
    private String addedAt;
    @JsonProperty
    private Album album;

    public SavedAlbum() {
    }

    public SavedAlbum(final String addedAt, final Album album) {
        this.addedAt = addedAt;
        this.album = album;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(final String addedAt) {
        this.addedAt = addedAt;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(final Album album) {
        this.album = album;
    }
}
