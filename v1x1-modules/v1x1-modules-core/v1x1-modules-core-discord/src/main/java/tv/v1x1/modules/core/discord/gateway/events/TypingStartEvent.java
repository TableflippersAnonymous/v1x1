package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.channel.ChannelId;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class TypingStartEvent extends DispatchPayload {
    @JsonProperty("d")
    private Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data extends ChannelId {
        @JsonProperty("user_id")
        private String userId;
        @JsonProperty
        private Integer timestamp;

        public Data() {
        }

        public Data(final String channelId, final String userId, final Integer timestamp) {
            super(channelId);
            this.userId = userId;
            this.timestamp = timestamp;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(final String userId) {
            this.userId = userId;
        }

        public Integer getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(final Integer timestamp) {
            this.timestamp = timestamp;
        }
    }

    public TypingStartEvent() {
    }

    public TypingStartEvent(final Long sequenceNumber, final Data data) {
        super(sequenceNumber, "TYPING_START");
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(final Data data) {
        this.data = data;
    }
}
