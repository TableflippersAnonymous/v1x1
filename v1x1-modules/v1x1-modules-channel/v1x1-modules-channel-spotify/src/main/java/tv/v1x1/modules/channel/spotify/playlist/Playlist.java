package tv.v1x1.modules.channel.spotify.playlist;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RPriorityBlockingDeque;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.services.spotify.SpotifyApi;
import tv.v1x1.common.services.spotify.dto.SimpleArtist;
import tv.v1x1.common.services.spotify.dto.Track;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

public class Playlist {
    private static final ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());
    private final SpotifyApi api;
    private final Channel channel;
    private final RPriorityBlockingDeque<byte[]> priorityBlockingDeque;

    public Playlist(final SpotifyApi api, final Channel channel, final RPriorityBlockingDeque<byte[]> priorityBlockingDeque) {
        this.api = api;
        this.channel = channel;
        this.priorityBlockingDeque = priorityBlockingDeque;
    }

    public PlaylistEntry add(final GlobalUser globalUser, final String spotifyUri) {
        final Track track = api.getTracks().getTrack(api.getIdFromUri(spotifyUri), Optional.of("from_token"));
        final PlaylistEntry playlistEntry = new PlaylistEntry(
                track.getUri(),
                track.getName(),
                track.getArtists().stream().map(SimpleArtist::getName).collect(Collectors.joining(", ")),
                globalUser.getId().toString(),
                new Date().getTime()
        );
        priorityBlockingDeque.add(serialize(playlistEntry));
        return playlistEntry;
    }

    private byte[] serialize(final PlaylistEntry playlistEntry) {
        try {
            return MAPPER.writeValueAsBytes(playlistEntry);
        } catch (final JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
