package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Playlists {
    @JsonProperty
    private Paging<SimplePlaylist> playlists;

    public Playlists() {
    }

    public Playlists(final Paging<SimplePlaylist> playlists) {
        this.playlists = playlists;
    }

    public Paging<SimplePlaylist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(final Paging<SimplePlaylist> playlists) {
        this.playlists = playlists;
    }
}
