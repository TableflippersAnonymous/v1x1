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
public class MessageDeleteEvent extends DispatchPayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MessageId extends ChannelId {
        @JsonProperty
        private String id;

        public MessageId() {
        }

        public MessageId(final String channelId, final String id) {
            super(channelId);
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }
    }

    @JsonProperty("d")
    private MessageId messageId;

    public MessageDeleteEvent() {
    }

    public MessageDeleteEvent(final Long sequenceNumber, final MessageId messageId) {
        super(sequenceNumber, "MESSAGE_DELETE");
        this.messageId = messageId;
    }

    public MessageId getMessageId() {
        return messageId;
    }

    public void setMessageId(final MessageId messageId) {
        this.messageId = messageId;
    }
}
