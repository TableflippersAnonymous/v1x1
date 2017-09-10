package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize(using = DispatchDeserializer.class)
public abstract class DispatchPayload extends Payload {
    @JsonProperty("s")
    private Long sequenceNumber;
    @JsonProperty("t")
    private String eventType;

    public DispatchPayload() {
    }

    public DispatchPayload(final Long sequenceNumber, final String eventType) {
        super(0);
        this.sequenceNumber = sequenceNumber;
        this.eventType = eventType;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(final Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }
}
