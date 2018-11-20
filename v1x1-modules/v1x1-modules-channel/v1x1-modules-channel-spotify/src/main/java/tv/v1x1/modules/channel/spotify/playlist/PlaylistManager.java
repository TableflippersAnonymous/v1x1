package tv.v1x1.modules.channel.spotify.playlist;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.rpc.client.SchedulerServiceClient;
import tv.v1x1.common.services.spotify.SpotifyApi;
import tv.v1x1.common.services.spotify.dto.RefreshResponse;
import tv.v1x1.modules.channel.spotify.SpotifyModule;
import tv.v1x1.modules.channel.spotify.SpotifyUserConfiguration;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class PlaylistManager {
    private final RedissonClient redisson;
    private final SpotifyModule module;
    private final RMapCache<byte[], byte[]> tokenCache;

    @Inject
    public PlaylistManager(final RedissonClient redisson, final SpotifyModule module) {
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
        return new Playlist(spotifyApi, module.getTwitchApi(), channel, userConfiguration, new tv.v1x1.common.dto.core.UUID(playlistId),
                redisson.getPriorityBlockingDeque("Modules|Channel|Spotify|Playlists|" + playlistId, ByteArrayCodec.INSTANCE),
                redisson.getMapCache("Modules|Channel|Spotify|Settings|" + playlistId, ByteArrayCodec.INSTANCE),
                redisson.getSetCache("Modules|Channel|Spotify|RecentlyPlayed|" + playlistId, ByteArrayCodec.INSTANCE),
                module.getServiceClient(SchedulerServiceClient.class));
    }

    private String getOAuthFromRefresh(final String refreshToken) {
        final byte[] oauthToken = tokenCache.get(refreshToken.getBytes());
        if(oauthToken != null)
            return new String(oauthToken);
        final SpotifyApi spotifyApi = new SpotifyApi(new String(module.requireCredential("Common|Spotify|ClientId")),
                null, new String(module.requireCredential("Common|Spotify|ClientSecret")),
                new String(module.requireCredential("Common|Spotify|RedirectUri")));
        final RefreshResponse refreshResponse = spotifyApi.getOAuth2().refreshToken(refreshToken);
        tokenCache.fastPut(refreshToken.getBytes(), refreshResponse.getAccessToken().getBytes(),
                refreshResponse.getExpiresIn() - 300, TimeUnit.SECONDS);
        return refreshResponse.getAccessToken();
    }
}
