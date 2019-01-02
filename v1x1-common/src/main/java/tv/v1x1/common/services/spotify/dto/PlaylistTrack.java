package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistTrack {
    @JsonProperty("added_at")
    private String addedAt;
    @JsonProperty("added_by")
    private PublicUser addedBy;
    @JsonProperty("is_local")
    private boolean isLocal;
    @JsonProperty
    private Track track;

    public PlaylistTrack() {
    }

    public PlaylistTrack(final String addedAt, final PublicUser addedBy, final boolean isLocal, final Track track) {
        this.addedAt = addedAt;
        this.addedBy = addedBy;
        this.isLocal = isLocal;
        this.track = track;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(final String addedAt) {
        this.addedAt = addedAt;
    }

    public PublicUser getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(final PublicUser addedBy) {
        this.addedBy = addedBy;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(final boolean local) {
        isLocal = local;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(final Track track) {
        this.track = track;
    }
}
