package tv.v1x1.modules.channel.spotify.playlist;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RPriorityBlockingDeque;
import org.redisson.api.RSetCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.rpc.client.SchedulerServiceClient;
import tv.v1x1.common.services.spotify.SpotifyApi;
import tv.v1x1.common.services.spotify.dto.CurrentlyPlayingContext;
import tv.v1x1.common.services.spotify.dto.PlayRequest;
import tv.v1x1.common.services.spotify.dto.SimpleArtist;
import tv.v1x1.common.services.spotify.dto.Track;
import tv.v1x1.common.services.spotify.dto.Tracks;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.modules.channel.spotify.SpotifyUserConfiguration;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Playlist {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());
    private static final int MAX_TIMER_MS = 30000;
    private static final int CHECK_AFTER_START_MS = 15000;
    private static final int INTER_TRACK_TOLERANCE_MS = 100;
    private static final String SILENT_TRACK_URI = "spotify:track:5XSKC4d0y0DfcGbvDOiL93";

    private final SpotifyApi api;
    private final TwitchApi twitchApi;
    private final Channel channel;
    private final SpotifyUserConfiguration userConfiguration;
    private final UUID playlistId;
    private final RPriorityBlockingDeque<byte[]> priorityBlockingDeque;
    private final RMapCache<byte[], byte[]> settings;
    private final RSetCache<byte[]> recentlyPlayed;
    private final SchedulerServiceClient schedulerServiceClient;

    public Playlist(final SpotifyApi api, final TwitchApi twitchApi, final Channel channel,
                    final SpotifyUserConfiguration userConfiguration, final UUID playlistId,
                    final RPriorityBlockingDeque<byte[]> priorityBlockingDeque,
                    final RMapCache<byte[], byte[]> settings, final RSetCache<byte[]> recentlyPlayed,
                    final SchedulerServiceClient schedulerServiceClient) {
        this.api = api;
        this.twitchApi = twitchApi;
        this.channel = channel;
        this.userConfiguration = userConfiguration;
        this.playlistId = playlistId;
        this.priorityBlockingDeque = priorityBlockingDeque;
        this.settings = settings;
        this.recentlyPlayed = recentlyPlayed;
        this.schedulerServiceClient = schedulerServiceClient;
    }

    public void start() {
        final CurrentlyPlayingContext currentlyPlayingContext = api.getPlayer().getCurrentlyPlayingContext(Optional.of("from_token"));
        setEnabled(true);
        if(currentlyPlayingContext == null || !currentlyPlayingContext.isPlaying())
            playNext();
        else
            setTimer(currentlyPlayingContext.getItem().getDurationMs() - currentlyPlayingContext.getProgressMs());
    }

    public void pause() {
        setEnabled(false);
        api.getPlayer().pause(Optional.empty());
    }

    public void stop() {
        pause();
        purge();
    }

    public void purge() {
        priorityBlockingDeque.clear();
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
        priorityBlockingDeque.addLast(serialize(playlistEntry));
        LOG.info("Added song to playlist: channel={} song={}", channel, playlistEntry);
        return playlistEntry;
    }

    public PlaylistEntry getNext() {
        return deserialize(priorityBlockingDeque.getFirst());
    }

    public void playNext() {
        if(!isEnabled())
            return;
        List<String> spotifyUris;
        try {
            final PlaylistEntry playlistEntry = deserialize(priorityBlockingDeque.removeFirst());
            final String spotifyUri = playlistEntry.getSpotifyUri();
            recentlyPlayed.add(api.getIdFromUri(spotifyUri).getBytes(), 15, TimeUnit.MINUTES);
            spotifyUris = ImmutableList.<String>builder().add(spotifyUri).add(SILENT_TRACK_URI).addAll(getDefaultNext()).build();
        } catch(final NoSuchElementException e) {
            spotifyUris = getDefaultNext();
        }
        if(spotifyUris.size() == 1) {
            api.getPlayer().shuffle(true, Optional.empty());
            api.getPlayer().repeat("context", Optional.empty());
        } else {
            api.getPlayer().shuffle(false, Optional.empty());
            api.getPlayer().repeat("off", Optional.empty());
        }
        LOG.info("Playing: channel={} spotifyQueue={}", channel, spotifyUris);
        api.getPlayer().play(Optional.empty(), new PlayRequest(spotifyUris, 0));
        setTimer(CHECK_AFTER_START_MS);
    }

    private List<String> getDefaultNext() {
        if(userConfiguration.getDefaultPlaylist() == null || userConfiguration.getDefaultPlaylist().isEmpty()) {
            return ImmutableList.of(getRecommendedTrack(), SILENT_TRACK_URI);
        } else {
            return ImmutableList.of(userConfiguration.getDefaultPlaylist());
        }
    }

    private String getRecommendedTrack() {
        while(recentlyPlayed.size() > 10)
            recentlyPlayed.iterator(1).remove();
        final Tracks tracks = api.getBrowse().getRecommendations(Optional.of(1), Optional.of("from_token"),
                ImmutableMap.of(), ImmutableMap.of(), ImmutableMap.of(), Optional.empty(), Optional.empty(),
                Optional.of(recentlyPlayed.stream().map(String::new).collect(Collectors.toList())));
        if(tracks.getTracks().isEmpty())
            return SILENT_TRACK_URI;
        return tracks.getTracks().get(0).getUri();
    }

    public void handleTimer() {
        final CurrentlyPlayingContext currentlyPlayingContext = api.getPlayer().getCurrentlyPlayingContext(Optional.of("from_token"));
        if(currentlyPlayingContext == null || !currentlyPlayingContext.isPlaying()) {
            setEnabled(false);
            return;
        }
        if(channel instanceof TwitchChannel && !userConfiguration.isAlwaysOn()
                && twitchApi.getStreams().getStream(channel.getChannelGroup().getId()).getStream() == null) {
            setEnabled(false);
            return;
        }
        final int leftMs = currentlyPlayingContext.getItem().getDurationMs() - currentlyPlayingContext.getProgressMs();
        if(leftMs < INTER_TRACK_TOLERANCE_MS || currentlyPlayingContext.getProgressMs() < INTER_TRACK_TOLERANCE_MS
                || currentlyPlayingContext.getItem().getUri().equals(SILENT_TRACK_URI))
            playNext();
        else
            setTimer(leftMs);
    }

    private void setTimer(int milliseconds) {
        if(milliseconds > MAX_TIMER_MS)
            milliseconds = MAX_TIMER_MS;
        schedulerServiceClient.scheduleWithDelay(milliseconds - INTER_TRACK_TOLERANCE_MS, playlistId,
                Channel.VAL_CODEC.encode(channel));
    }

    private void setEnabled(final boolean enabled) {
        settings.fastPut("enabled".getBytes(), new byte[] { (byte) (enabled ? 1 : 0) }, 24, TimeUnit.HOURS);
    }

    private boolean isEnabled() {
        final byte[] bytes = settings.get("enabled".getBytes());
        if(bytes == null || bytes.length < 1)
            return false;
        return bytes[0] == 1;
    }

    private byte[] serialize(final PlaylistEntry playlistEntry) {
        try {
            return MAPPER.writeValueAsBytes(playlistEntry);
        } catch (final JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private PlaylistEntry deserialize(final byte[] bytes) {
        try {
            return MAPPER.readValue(bytes, PlaylistEntry.class);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
