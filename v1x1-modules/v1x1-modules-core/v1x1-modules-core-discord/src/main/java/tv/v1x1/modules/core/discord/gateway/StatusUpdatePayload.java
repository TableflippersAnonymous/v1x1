package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.user.StatusUpdate;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class StatusUpdatePayload extends Payload {
    @JsonProperty("d")
    private StatusUpdate statusUpdate;

    public StatusUpdatePayload() {
    }

    public StatusUpdatePayload(final StatusUpdate statusUpdate) {
        super(3);
        this.statusUpdate = statusUpdate;
    }

    public StatusUpdate getStatusUpdate() {
        return statusUpdate;
    }

    public void setStatusUpdate(final StatusUpdate statusUpdate) {
        this.statusUpdate = statusUpdate;
    }
}
