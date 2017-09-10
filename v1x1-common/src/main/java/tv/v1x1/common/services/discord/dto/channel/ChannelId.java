package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelId {
    @JsonProperty("channel_id")
    private String channelId;

    public ChannelId() {
    }

    public ChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }
}
