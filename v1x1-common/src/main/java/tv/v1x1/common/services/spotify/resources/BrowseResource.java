package tv.v1x1.common.services.spotify.resources;

import com.google.common.base.Joiner;
import tv.v1x1.common.services.spotify.SpotifyApi;
import tv.v1x1.common.services.spotify.dto.AlbumsPaging;
import tv.v1x1.common.services.spotify.dto.Categories;
import tv.v1x1.common.services.spotify.dto.Category;
import tv.v1x1.common.services.spotify.dto.Playlists;
import tv.v1x1.common.services.spotify.dto.Tracks;

import javax.ws.rs.client.WebTarget;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BrowseResource {
    private final WebTarget browse;
    private final WebTarget recommendations;

    public BrowseResource(final WebTarget browse, final WebTarget recommendations) {
        this.browse = browse;
        this.recommendations = recommendations;
    }

    public Category getCategory(final String id, final Optional<String> country, final Optional<String> locale) {
        return browse.path("categories").path(id)
                .queryParam("country", country.orElse(null))
                .queryParam("locale", locale.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(Category.class);
    }

    public Playlists getCategoryPlaylists(final String id, final Optional<String> country,
                                          final Optional<Integer> limit, final Optional<Integer> offset) {
        return browse.path("categories").path(id).path("playlists")
                .queryParam("country", country.orElse(null))
                .queryParam("limit", limit.orElse(null))
                .queryParam("offset", offset.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(Playlists.class);
    }

    public Categories listCategories(final Optional<String> country, final Optional<String> locale,
                                     final Optional<Integer> limit, final Optional<Integer> offset) {
        return browse.path("categories")
                .queryParam("country", country.orElse(null))
                .queryParam("locale", locale.orElse(null))
                .queryParam("limit", limit.orElse(null))
                .queryParam("offset", offset.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(Categories.class);
    }

    public Playlists getFeaturedPlaylists(final Optional<String> country, final Optional<String> locale,
                                          final Optional<String> timestamp, final Optional<Integer> limit,
                                          final Optional<Integer> offset) {
        return browse.path("featured-playlists")
                .queryParam("country", country.orElse(null))
                .queryParam("locale", locale.orElse(null))
                .queryParam("timestamp", timestamp.orElse(null))
                .queryParam("limit", limit.orElse(null))
                .queryParam("offset", offset.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(Playlists.class);
    }

    public AlbumsPaging listNewReleases(final Optional<String> country, final Optional<Integer> limit,
                                        final Optional<Integer> offset) {
        return browse.path("new-releases")
                .queryParam("country", country.orElse(null))
                .queryParam("limit", limit.orElse(null))
                .queryParam("offset", offset.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(AlbumsPaging.class);
    }

    public Tracks getRecommendations(final Optional<Integer> limit, final Optional<String> market,
                                     final Map<String, Float> max, final Map<String, Float> min,
                                     final Map<String, Float> target, final Optional<List<String>> seedArtists,
                                     final Optional<List<String>> seedGenres, final Optional<List<String>> seedTracks) {
        WebTarget webTarget = recommendations
                .queryParam("limit", limit.orElse(null))
                .queryParam("market", market.orElse(null))
                .queryParam("seed_artists", seedArtists.map(entry -> Joiner.on(",").join(entry)).orElse(null))
                .queryParam("seed_genres", seedGenres.map(entry -> Joiner.on(",").join(entry)).orElse(null))
                .queryParam("seed_tracks", seedTracks.map(entry -> Joiner.on(",").join(entry)).orElse(null));
        for(final Map.Entry<String, Float> entry : max.entrySet())
            webTarget = webTarget.queryParam("max_" + entry.getKey(), entry.getValue());
        for(final Map.Entry<String, Float> entry : min.entrySet())
            webTarget = webTarget.queryParam("min_" + entry.getKey(), entry.getValue());
        for(final Map.Entry<String, Float> entry : target.entrySet())
            webTarget = webTarget.queryParam("target_" + entry.getKey(), entry.getValue());
        return webTarget.request(SpotifyApi.ACCEPT)
                .get(Tracks.class);
    }
}
