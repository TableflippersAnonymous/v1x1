package tv.v1x1.common.services.spotify.resources;

import com.google.common.base.Joiner;
import tv.v1x1.common.services.spotify.SpotifyApi;
import tv.v1x1.common.services.spotify.dto.Album;
import tv.v1x1.common.services.spotify.dto.Albums;
import tv.v1x1.common.services.spotify.dto.Paging;
import tv.v1x1.common.services.spotify.dto.SimpleTrack;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;
import java.util.Optional;

public class AlbumsResource {
    private final WebTarget albums;

    public AlbumsResource(final WebTarget albums) {
        this.albums = albums;
    }

    public Album getAlbum(final String id, final Optional<String> market) {
        return albums.path(id)
                .queryParam("market", market.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(Album.class);
    }

    public Paging<SimpleTrack> getAlbumTracks(final String id, final Optional<Integer> limit,
                                              final Optional<Integer> offset, final Optional<String> market) {
        return albums.path(id).path("tracks")
                .queryParam("limit", limit.orElse(null))
                .queryParam("offset", offset.orElse(null))
                .queryParam("market", market.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(new GenericType<Paging<SimpleTrack>>() {});
    }

    public Albums getAlbums(final List<String> ids, final Optional<String> market) {
        return albums.queryParam("ids", Joiner.on(",").join(ids))
                .queryParam("market", market.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(Albums.class);
    }
}
