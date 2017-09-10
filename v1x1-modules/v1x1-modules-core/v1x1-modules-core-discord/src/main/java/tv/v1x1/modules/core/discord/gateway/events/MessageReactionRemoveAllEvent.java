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
public class MessageReactionRemoveAllEvent extends DispatchPayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MessageId extends ChannelId {
        @JsonProperty("message_id")
        private String messageId;

        public MessageId() {
        }

        public MessageId(final String channelId, final String messageId) {
            super(channelId);
            this.messageId = messageId;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(final String messageId) {
            this.messageId = messageId;
        }
    }

    @JsonProperty("d")
    private MessageId messageId;

    public MessageReactionRemoveAllEvent() {
    }

    public MessageReactionRemoveAllEvent(final Long sequenceNumber, final MessageId messageId) {
        super(sequenceNumber, "MESSAGE_REACTION_REMOVE_ALL");
        this.messageId = messageId;
    }

    public MessageId getMessageId() {
        return messageId;
    }

    public void setMessageId(final MessageId messageId) {
        this.messageId = messageId;
    }
}
