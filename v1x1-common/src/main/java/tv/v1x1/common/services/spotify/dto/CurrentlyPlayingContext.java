package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentlyPlayingContext {
    @JsonProperty
    private Device device;
    @JsonProperty("repeat_state")
    private String repeatState;
    @JsonProperty("shuffle_state")
    private boolean shuffleState;
    @JsonProperty
    private Context context;
    @JsonProperty
    private int timestamp;
    @JsonProperty("progress_ms")
    private int progressMs;
    @JsonProperty("is_playing")
    private boolean isPlaying;
    @JsonProperty
    private Track item;
    @JsonProperty("currently_playing_type")
    private String currentlyPlayingType;

    public CurrentlyPlayingContext() {
    }

    public CurrentlyPlayingContext(final Device device, final String repeatState, final boolean shuffleState,
                                   final Context context, final int timestamp, final int progressMs,
                                   final boolean isPlaying, final Track item, final String currentlyPlayingType) {
        this.device = device;
        this.repeatState = repeatState;
        this.shuffleState = shuffleState;
        this.context = context;
        this.timestamp = timestamp;
        this.progressMs = progressMs;
        this.isPlaying = isPlaying;
        this.item = item;
        this.currentlyPlayingType = currentlyPlayingType;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(final Device device) {
        this.device = device;
    }

    public String getRepeatState() {
        return repeatState;
    }

    public void setRepeatState(final String repeatState) {
        this.repeatState = repeatState;
    }

    public boolean isShuffleState() {
        return shuffleState;
    }

    public void setShuffleState(final boolean shuffleState) {
        this.shuffleState = shuffleState;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(final Context context) {
        this.context = context;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    public int getProgressMs() {
        return progressMs;
    }

    public void setProgressMs(final int progressMs) {
        this.progressMs = progressMs;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(final boolean playing) {
        isPlaying = playing;
    }

    public Track getItem() {
        return item;
    }

    public void setItem(final Track item) {
        this.item = item;
    }

    public String getCurrentlyPlayingType() {
        return currentlyPlayingType;
    }

    public void setCurrentlyPlayingType(final String currentlyPlayingType) {
        this.currentlyPlayingType = currentlyPlayingType;
    }
}
