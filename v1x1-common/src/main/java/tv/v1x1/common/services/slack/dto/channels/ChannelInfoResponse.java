package tv.v1x1.common.services.slack.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;

public class ChannelInfoResponse extends Response {
    @JsonProperty
    private Channel channel;

    public ChannelInfoResponse() {
        super(true);
    }

    public ChannelInfoResponse(final Channel channel) {
        super(true);
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(final Channel channel) {
        this.channel = channel;
    }
}
