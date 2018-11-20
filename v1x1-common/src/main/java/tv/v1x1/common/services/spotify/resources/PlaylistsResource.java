package tv.v1x1.common.services.spotify.resources;

import tv.v1x1.common.services.spotify.SpotifyApi;
import tv.v1x1.common.services.spotify.dto.Paging;
import tv.v1x1.common.services.spotify.dto.PlaylistTrack;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.Optional;

public class PlaylistsResource {
    private final WebTarget playlists;

    public PlaylistsResource(final WebTarget playlists) {
        this.playlists = playlists;
    }

    public Paging<PlaylistTrack> getPlaylistTracks(final String id, final Optional<Integer> limit,
                                                   final Optional<Integer> offset, final Optional<String> market) {
        return playlists.path(id).path("tracks")
                .queryParam("limit", limit.orElse(null))
                .queryParam("offset", offset.orElse(null))
                .queryParam("market", market.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(new GenericType<Paging<PlaylistTrack>>() {});
    }
}
