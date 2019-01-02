package tv.v1x1.common.services.spotify.resources;

import com.google.common.base.Joiner;
import tv.v1x1.common.services.spotify.SpotifyApi;
import tv.v1x1.common.services.spotify.dto.Album;
import tv.v1x1.common.services.spotify.dto.Artist;
import tv.v1x1.common.services.spotify.dto.Artists;
import tv.v1x1.common.services.spotify.dto.Paging;
import tv.v1x1.common.services.spotify.dto.Tracks;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;
import java.util.Optional;

public class ArtistsResource {
    private final WebTarget artists;

    public ArtistsResource(final WebTarget artists) {
        this.artists = artists;
    }

    public Artist getArtist(final String id, final Optional<String> market) {
        return artists.path(id)
                .queryParam("market", market.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(Artist.class);
    }

    public Paging<Album> getAlbumsByArtist(final String id, final Optional<List<String>> includeGroups,
                                           final Optional<String> market, final Optional<Integer> limit,
                                           final Optional<Integer> offset) {
        return artists.path(id).path("albums")
                .queryParam("include_groups", includeGroups.map(group -> Joiner.on(",").join(group)).orElse(null))
                .queryParam("market", market.orElse(null))
                .queryParam("limit", limit.orElse(null))
                .queryParam("offset", offset.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(new GenericType<Paging<Album>>() {});
    }

    public Tracks getTopTracks(final String id, final Optional<String> market) {
        return artists.path(id).path("top-tracks")
                .queryParam("market", market.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(Tracks.class);
    }

    public Artists getRelatedArtists(final String id) {
        return artists.path(id).path("related-artists")
                .request(SpotifyApi.ACCEPT)
                .get(Artists.class);
    }

    public Artists getArtists(final List<String> ids) {
        return artists.queryParam("ids", Joiner.on(",").join(ids))
                .request(SpotifyApi.ACCEPT)
                .get(Artists.class);
    }
}
