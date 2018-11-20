package tv.v1x1.modules.channel.spotify.playlist;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.redisson.api.RDeque;
import org.redisson.api.RMapCache;
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
import tv.v1x1.common.services.spotify.dto.Paging;
import tv.v1x1.common.services.spotify.dto.PlayRequest;
import tv.v1x1.common.services.spotify.dto.PlaylistTrack;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Playlist {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());
    private static final Random RANDOM = new Random();
    private static final int MAX_TIMER_MS = 30000;
    private static final int CHECK_AFTER_START_MS = 15000;
    private static final int INTER_TRACK_TOLERANCE_MS = 1000;
    private static final String SILENT_TRACK_URI = "spotify:track:5XSKC4d0y0DfcGbvDOiL93";
    private static final List<String> DEFAULT_SEED_GENRES = ImmutableList.of("edm");

    private final SpotifyApi api;
    private final TwitchApi twitchApi;
    private final Channel channel;
    private final SpotifyUserConfiguration userConfiguration;
    private final UUID playlistId;
    private final RDeque<byte[]> deque;
    private final RMapCache<byte[], byte[]> settings;
    private final RSetCache<byte[]> recentlyPlayed;
    private final SchedulerServiceClient schedulerServiceClient;

    public Playlist(final SpotifyApi api, final TwitchApi twitchApi, final Channel channel,
                    final SpotifyUserConfiguration userConfiguration, final UUID playlistId,
                    final RDeque<byte[]> deque,
                    final RMapCache<byte[], byte[]> settings, final RSetCache<byte[]> recentlyPlayed,
                    final SchedulerServiceClient schedulerServiceClient) {
        this.api = api;
        this.twitchApi = twitchApi;
        this.channel = channel;
        this.userConfiguration = userConfiguration;
        this.playlistId = playlistId;
        this.deque = deque;
        this.settings = settings;
        this.recentlyPlayed = recentlyPlayed;
        this.schedulerServiceClient = schedulerServiceClient;
    }

    public void start() {
        final CurrentlyPlayingContext currentlyPlayingContext = api.getPlayer().getCurrentlyPlayingContext(Optional.of("from_token"));
        setEnabled(true);
        api.getPlayer().shuffle(false, Optional.empty());
        api.getPlayer().repeat("off", Optional.empty());
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
        deque.clear();
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
        deque.add(serialize(playlistEntry));
        LOG.info("Added song to playlist: channel={} song={}", channel, playlistEntry);
        return playlistEntry;
    }

    public PlaylistEntry getNext() {
        return deserialize(deque.getFirst());
    }

    public void playNext() {
        if(!isEnabled())
            return;
        List<String> spotifyUris;
        try {
            final PlaylistEntry playlistEntry = deserialize(deque.removeFirst());
            final String spotifyUri = playlistEntry.getSpotifyUri();
            recentlyPlayed.add(api.getIdFromUri(spotifyUri).getBytes(), 15, TimeUnit.MINUTES);
            spotifyUris = ImmutableList.<String>builder().add(spotifyUri).add(SILENT_TRACK_URI).addAll(getDefaultNext()).build();
        } catch(final NoSuchElementException e) {
            spotifyUris = getDefaultNext();
        }
        LOG.info("Playing: channel={} spotifyQueue={}", channel, spotifyUris);
        api.getPlayer().play(Optional.empty(), new PlayRequest(spotifyUris, 0));
        setTimer(CHECK_AFTER_START_MS);
    }

    public void handleTimer() {
        LOG.info("Got timer interrupt for {}", channel);
        final CurrentlyPlayingContext currentlyPlayingContext = api.getPlayer().getCurrentlyPlayingContext(Optional.of("from_token"));
        if(currentlyPlayingContext == null || !currentlyPlayingContext.isPlaying()) {
            LOG.debug("Not currently playing, disabling for {}", channel);
            setEnabled(false);
            return;
        }
        if(channel instanceof TwitchChannel && !userConfiguration.isAlwaysOn()
                && twitchApi.getStreams().getStream(channel.getChannelGroup().getId()).getStream() == null) {
            LOG.debug("Stream went offline, disabling for {}", channel);
            setEnabled(false);
            return;
        }
        final long leftMs = currentlyPlayingContext.getItem().getDurationMs() - currentlyPlayingContext.getProgressMs();
        if(leftMs < INTER_TRACK_TOLERANCE_MS || currentlyPlayingContext.getProgressMs() < INTER_TRACK_TOLERANCE_MS
                || currentlyPlayingContext.getItem().getUri().equals(SILENT_TRACK_URI)) {
            LOG.info("Within inter-track, injecting next song.");
            playNext();
        } else {
            LOG.debug("Not within inter-track, trying again in {}ms", leftMs);
            setTimer(leftMs);
        }
    }

    private List<String> getDefaultNext() {
        if(userConfiguration.getDefaultPlaylist() == null || userConfiguration.getDefaultPlaylist().isEmpty()) {
            return ImmutableList.of(getRecommendedTrack(), SILENT_TRACK_URI);
        } else {
            return ImmutableList.of(getRandomPlaylistSong(userConfiguration.getDefaultPlaylist()), SILENT_TRACK_URI);
        }
    }

    private String getRandomPlaylistSong(final String playlistUri) {
        final String[] parts = playlistUri.split(":");
        final String playlistId = parts[parts.length - 1];
        final Paging<PlaylistTrack> playlistEntries = api.getPlaylists().getPlaylistTracks(playlistId, Optional.of(100),
                Optional.empty(), Optional.of("from_token"));
        if(playlistEntries.getItems().isEmpty())
            return getRecommendedTrack();
        final int idx = RANDOM.nextInt(playlistEntries.getItems().size());
        return playlistEntries.getItems().get(idx).getTrack().getUri();
    }

    private String getRecommendedTrack() {
        while(recentlyPlayed.size() > 10)
            recentlyPlayed.iterator(1).remove();
        final List<String> recentlyPlayedList = recentlyPlayed.stream().map(String::new).collect(Collectors.toList());
        final Tracks tracks = api.getBrowse().getRecommendations(Optional.of(1), Optional.of("from_token"),
                ImmutableMap.of(), ImmutableMap.of(), ImmutableMap.of(), Optional.empty(),
                recentlyPlayedList.isEmpty() ? Optional.of(DEFAULT_SEED_GENRES) :Optional.empty(),
                recentlyPlayedList.isEmpty() ? Optional.empty() : Optional.of(recentlyPlayedList));
        if(tracks.getTracks().isEmpty())
            return SILENT_TRACK_URI;
        return tracks.getTracks().get(0).getUri();
    }

    private void setTimer(long milliseconds) {
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
