package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.channel.Message;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class MessageCreateEvent extends DispatchPayload {
    @JsonProperty("d")
    private Message message;

    public MessageCreateEvent() {
    }

    public MessageCreateEvent(final Long sequenceNumber, final Message message) {
        super(sequenceNumber, "MESSAGE_CREATE");
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(final Message message) {
        this.message = message;
    }
}
