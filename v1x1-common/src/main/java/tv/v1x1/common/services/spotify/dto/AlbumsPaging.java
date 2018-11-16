package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumsPaging {
    @JsonProperty
    private Paging<SimpleAlbum> albums;

    public AlbumsPaging() {
    }

    public AlbumsPaging(final Paging<SimpleAlbum> albums) {
        this.albums = albums;
    }

    public Paging<SimpleAlbum> getAlbums() {
        return albums;
    }

    public void setAlbums(final Paging<SimpleAlbum> albums) {
        this.albums = albums;
    }
}
