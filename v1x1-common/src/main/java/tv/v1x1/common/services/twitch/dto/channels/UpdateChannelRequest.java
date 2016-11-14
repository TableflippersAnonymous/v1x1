package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.channels.ChannelRequest;

/**
 * Created by naomi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
