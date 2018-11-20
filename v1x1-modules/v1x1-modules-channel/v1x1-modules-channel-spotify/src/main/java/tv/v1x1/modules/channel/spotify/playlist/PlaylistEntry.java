package tv.v1x1.modules.channel.spotify.playlist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistEntry {
    @JsonProperty("spotify_uri")
    private String spotifyUri;
    @JsonProperty
    private String title;
    @JsonProperty
    private String artist;
    @JsonProperty("global_user_id")
    private String globalUserId;
    @JsonProperty
    private long timestamp;

    public PlaylistEntry() {
    }

    public PlaylistEntry(final String spotifyUri, final String title, final String artist, final String globalUserId,
                         final long timestamp) {
        this.spotifyUri = spotifyUri;
        this.title = title;
        this.artist = artist;
        this.globalUserId = globalUserId;
        this.timestamp = timestamp;
    }

    public String getSpotifyUri() {
        return spotifyUri;
    }

    public void setSpotifyUri(final String spotifyUri) {
        this.spotifyUri = spotifyUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(final String artist) {
        this.artist = artist;
    }

    public String getGlobalUserId() {
        return globalUserId;
    }

    public void setGlobalUserId(final String globalUserId) {
        this.globalUserId = globalUserId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PlaylistEntry{" +
                "spotifyUri='" + spotifyUri + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", globalUserId='" + globalUserId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
