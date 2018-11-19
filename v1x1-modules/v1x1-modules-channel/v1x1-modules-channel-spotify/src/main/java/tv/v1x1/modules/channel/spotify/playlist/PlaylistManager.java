package tv.v1x1.modules.channel.spotify.playlist;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.redisson.Redisson;
import org.redisson.api.RMapCache;
import org.redisson.client.codec.ByteArrayCodec;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.rpc.client.SchedulerServiceClient;
import tv.v1x1.common.services.spotify.SpotifyApi;
import tv.v1x1.modules.channel.spotify.SpotifyModule;
import tv.v1x1.modules.channel.spotify.SpotifyUserConfiguration;

import java.util.UUID;

@Singleton
public class PlaylistManager {
    private final Redisson redisson;
    private final SpotifyModule module;
    private final RMapCache<byte[], byte[]> tokenCache;

    @Inject
    public PlaylistManager(final Redisson redisson, final SpotifyModule module) {
        this.redisson = redisson;
        this.tokenCache = redisson.getMapCache("Modules|Channel|Spotify|OAuthTokens", ByteArrayCodec.INSTANCE);
        this.module = module;
    }

    public Playlist getPlaylistFor(final Channel channel) {
        final SpotifyUserConfiguration userConfiguration = module.getConfiguration(channel);
        final String spotifyOAuth = getOAuthFromRefresh(userConfiguration.getRefreshToken());
        final UUID playlistId = UUID.nameUUIDFromBytes(spotifyOAuth.getBytes());
        final SpotifyApi spotifyApi = new SpotifyApi(new String(module.requireCredential("Common|Spotify|ClientId")),
                spotifyOAuth, new String(module.requireCredential("Common|Spotify|ClientSecret")),
                new String(module.requireCredential("Common|Spotify|RedirectUri")));
        return new Playlist(spotifyApi, channel, userConfiguration, new tv.v1x1.common.dto.core.UUID(playlistId),
                redisson.getPriorityBlockingDeque("Modules|Channel|Spotify|Playlists|" + playlistId, ByteArrayCodec.INSTANCE),
                redisson.getMapCache("Modules|Channel|Spotify|Settings|" + playlistId, ByteArrayCodec.INSTANCE),
                module.getServiceClient(SchedulerServiceClient.class));
    }

    private String getOAuthFromRefresh(final String refreshToken) {
        final byte[] oauthToken = tokenCache.get(refreshToken.getBytes());
        if(oauthToken != null)
            return new String(oauthToken);
        final SpotifyApi spotifyApi = new SpotifyApi(new String(module.requireCredential("Common|Spotify|ClientId")),
                "", new String(module.requireCredential("Common|Spotify|ClientSecret")),
                new String(module.requireCredential("Common|Spotify|RedirectUri")));
        //TODO: spotifyApi.getOAuth().refreshToken(refreshToken)
        return null;
    }
}
