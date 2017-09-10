package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.channel.Channel;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class ChannelUpdateEvent extends DispatchPayload {
    @JsonProperty("d")
    private Channel channel;

    public ChannelUpdateEvent() {
    }

    public ChannelUpdateEvent(final Long sequenceNumber, final Channel channel) {
        super(sequenceNumber, "CHANNEL_UPDATE");
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(final Channel channel) {
        this.channel = channel;
    }
}
