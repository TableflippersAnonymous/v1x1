package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/29/2016.
 */
public class ChannelRequest {
    @JsonProperty
    private String status;
    @JsonProperty
    private String game;
    @JsonProperty
    private Integer delay;
    @JsonProperty("channel_feed_enabled")
    private Boolean channelFeedEnabled;

    public ChannelRequest() {
    }

    public ChannelRequest(final String status, final String game, final Integer delay, final Boolean channelFeedEnabled) {
        this.status = status;
        this.game = game;
        this.delay = delay;
        this.channelFeedEnabled = channelFeedEnabled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getGame() {
        return game;
    }

    public void setGame(final String game) {
        this.game = game;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(final Integer delay) {
        this.delay = delay;
    }

    public Boolean getChannelFeedEnabled() {
        return channelFeedEnabled;
    }

    public void setChannelFeedEnabled(final Boolean channelFeedEnabled) {
        this.channelFeedEnabled = channelFeedEnabled;
    }
}
