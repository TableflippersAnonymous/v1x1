package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayHistory {
    @JsonProperty
    private SimpleTrack track;
    @JsonProperty("played_at")
    private String playedAt;
    @JsonProperty
    private Context context;

    public PlayHistory() {
    }

    public PlayHistory(final SimpleTrack track, final String playedAt, final Context context) {
        this.track = track;
        this.playedAt = playedAt;
        this.context = context;
    }

    public SimpleTrack getTrack() {
        return track;
    }

    public void setTrack(final SimpleTrack track) {
        this.track = track;
    }

    public String getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(final String playedAt) {
        this.playedAt = playedAt;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(final Context context) {
        this.context = context;
    }
}
