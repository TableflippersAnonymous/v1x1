package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class ChannelPinsUpdateEvent extends DispatchPayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("channel_id")
        private String channelId;
        @JsonProperty("last_pin_timestamp")
        private String lastPinTimestamp;

        public Data() {
        }

        public Data(final String channelId, final String lastPinTimestamp) {
            this.channelId = channelId;
            this.lastPinTimestamp = lastPinTimestamp;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(final String channelId) {
            this.channelId = channelId;
        }

        public String getLastPinTimestamp() {
            return lastPinTimestamp;
        }

        public void setLastPinTimestamp(final String lastPinTimestamp) {
            this.lastPinTimestamp = lastPinTimestamp;
        }
    }

    @JsonProperty("d")
    private Data data;

    public ChannelPinsUpdateEvent() {
    }

    public ChannelPinsUpdateEvent(final Long sequenceNumber, final Data data) {
        super(sequenceNumber, "CHANNEL_PINS_UPDATE");
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(final Data data) {
        this.data = data;
    }
}
