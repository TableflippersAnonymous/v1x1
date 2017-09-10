package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.channel.UserReaction;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class MessageReactionRemoveEvent extends DispatchPayload {
    @JsonProperty("d")
    private UserReaction reaction;

    public MessageReactionRemoveEvent() {
    }

    public MessageReactionRemoveEvent(final Long sequenceNumber, final UserReaction reaction) {
        super(sequenceNumber, "MESSAGE_REACTION_REMOVE");
        this.reaction = reaction;
    }

    public UserReaction getReaction() {
        return reaction;
    }

    public void setReaction(final UserReaction reaction) {
        this.reaction = reaction;
    }
}
