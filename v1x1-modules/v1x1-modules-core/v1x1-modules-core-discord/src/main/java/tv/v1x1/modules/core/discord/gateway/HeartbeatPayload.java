package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class HeartbeatPayload extends Payload {
    @JsonProperty("d")
    private Long lastSequenceNumberSeen;

    public HeartbeatPayload() {
    }

    public HeartbeatPayload(final Long lastSequenceNumberSeen) {
        super(1);
        this.lastSequenceNumberSeen = lastSequenceNumberSeen;
    }

    public Long getLastSequenceNumberSeen() {
        return lastSequenceNumberSeen;
    }

    public void setLastSequenceNumberSeen(final Long lastSequenceNumberSeen) {
        this.lastSequenceNumberSeen = lastSequenceNumberSeen;
    }
}
