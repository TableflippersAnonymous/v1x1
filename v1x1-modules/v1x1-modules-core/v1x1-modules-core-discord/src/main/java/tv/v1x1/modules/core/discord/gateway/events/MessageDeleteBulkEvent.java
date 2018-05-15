package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.channel.ChannelId;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

import java.util.List;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class MessageDeleteBulkEvent extends DispatchPayload {
    @JsonProperty("d")
    private MessageIds messageIds;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MessageIds extends ChannelId {
        @JsonProperty
        private List<String> ids;

        public MessageIds() {
        }

        public MessageIds(final String channelId, final List<String> ids) {
            super(channelId);
            this.ids = ids;
        }

        public List<String> getIds() {
            return ids;
        }

        public void setIds(final List<String> ids) {
            this.ids = ids;
        }
    }

    public MessageDeleteBulkEvent() {
    }

    public MessageDeleteBulkEvent(final Long sequenceNumber, final MessageIds messageIds) {
        super(sequenceNumber, "MESSAGE_DELETE_BULK");
        this.messageIds = messageIds;
    }

    public MessageIds getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(final MessageIds messageIds) {
        this.messageIds = messageIds;
    }
}
