package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.channels.ChannelRequest;

/**
 * Created by cobi on 10/29/2016.
 */
public class UpdateChannelRequest {
    @JsonProperty
    private ChannelRequest channel;

    public UpdateChannelRequest() {
    }

    public UpdateChannelRequest(final ChannelRequest channel) {
        this.channel = channel;
    }

    public ChannelRequest getChannel() {
        return channel;
    }

    public void setChannel(final ChannelRequest channel) {
        this.channel = channel;
    }
}
