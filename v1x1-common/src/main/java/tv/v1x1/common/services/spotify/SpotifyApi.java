package tv.v1x1.common.services.spotify;

import org.glassfish.jersey.jackson.JacksonFeature;
import tv.v1x1.common.services.spotify.resources.AlbumsResource;
import tv.v1x1.common.services.spotify.resources.ArtistsResource;
import tv.v1x1.common.services.spotify.resources.BrowseResource;
import tv.v1x1.common.services.spotify.resources.OAuth2Resource;
import tv.v1x1.common.services.spotify.resources.PlayerResource;
import tv.v1x1.common.services.spotify.resources.TracksResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class SpotifyApi {
    public static final String ACCEPT = "application/json";
    public static final String BASE_URL = "https://api.spotify.com/v1";
    public static final String ACCOUNTS_BASE_URL = "https://accounts.spotify.com/api";

    private final AlbumsResource albums;
    private final ArtistsResource artists;
    private final BrowseResource browse;
    private final PlayerResource player;
    private final TracksResource tracks;
    private final OAuth2Resource oAuth2;

    public SpotifyApi(final String clientId, final String oauthToken, final String clientSecret, final String redirectUri) {
        final SpotifyApiRequestFilter spotifyApiRequestFilter = new SpotifyApiRequestFilter(oauthToken);
        final Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        client.register(spotifyApiRequestFilter);
        final WebTarget api = client.target(BASE_URL);
        final WebTarget accountsApi = client.target(ACCOUNTS_BASE_URL);
        albums = new AlbumsResource(api.path("albums"));
        artists = new ArtistsResource(api.path("artists"));
        browse = new BrowseResource(api.path("browse"), api.path("recommendations"));
        player = new PlayerResource(api.path("player"));
        tracks = new TracksResource(api.path("tracks"), api.path("audio-features"), api.path("audio-analysis"));
        oAuth2 = new OAuth2Resource(accountsApi.path("token"), clientId, clientSecret, redirectUri);
    }

    public AlbumsResource getAlbums() {
        return albums;
    }

    public ArtistsResource getArtists() {
        return artists;
    }

    public BrowseResource getBrowse() {
        return browse;
    }

    public PlayerResource getPlayer() {
        return player;
    }

    public TracksResource getTracks() {
        return tracks;
    }

    public OAuth2Resource getOAuth2() {
        return oAuth2;
    }

    public String getIdFromUri(final String spotifyUri) {
        final String[] parts = spotifyUri.split(":");
        if(parts.length < 3)
            throw new IllegalArgumentException();
        return parts[2];
    }
}
