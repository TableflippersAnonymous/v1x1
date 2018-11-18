package tv.v1x1.modules.channel.spotify.playlist;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.redisson.Redisson;
import org.redisson.client.codec.ByteArrayCodec;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.modules.channel.spotify.SpotifyModule;

import java.util.UUID;

@Singleton
public class PlaylistManager {
    private final Redisson redisson;
    private final SpotifyModule module;

    @Inject
    public PlaylistManager(final Redisson redisson, final SpotifyModule module) {
        this.redisson = redisson;
        this.module = module;
    }

    public Playlist getPlaylistFor(final Channel channel) {
        final String spotifyOAuth = module.getConfiguration(channel).getSpotifyOAuth();
        final UUID playlistId = UUID.nameUUIDFromBytes(spotifyOAuth.getBytes());
        return new Playlist(module, channel, redisson.getPriorityBlockingDeque("Modules|Channel|Spotify|Playlists|" + playlistId, ByteArrayCodec.INSTANCE));
    }
}
