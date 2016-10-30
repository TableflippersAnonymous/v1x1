package tv.v1x1.common.services.twitch.dto.streams;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.misc.Image;
import tv.v1x1.common.services.twitch.dto.channels.Channel;

/**
 * Created by naomi on 10/30/2016.
 */
public class Stream {
    @JsonProperty
    private String game;
    @JsonProperty
    private long viewers;
    @JsonProperty("average_fps")
    private double averageFps;
    @JsonProperty
    private int delay;
    @JsonProperty("video_height")
    private int videoHeight;
    @JsonProperty("is_playlist")
    private boolean isPlaylist;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("_id")
    private long id;
    @JsonProperty
    private Channel channel;
    @JsonProperty
    private Image preview;

    public Stream() {
    }

    public String getGame() {
        return game;
    }

    public void setGame(final String game) {
        this.game = game;
    }

    public long getViewers() {
        return viewers;
    }

    public void setViewers(final long viewers) {
        this.viewers = viewers;
    }

    public double getAverageFps() {
        return averageFps;
    }

    public void setAverageFps(final double averageFps) {
        this.averageFps = averageFps;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(final int delay) {
        this.delay = delay;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(final int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public boolean isPlaylist() {
        return isPlaylist;
    }

    public void setPlaylist(final boolean playlist) {
        isPlaylist = playlist;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(final Channel channel) {
        this.channel = channel;
    }

    public Image getPreview() {
        return preview;
    }

    public void setPreview(final Image preview) {
        this.preview = preview;
    }
}
