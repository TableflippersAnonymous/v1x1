package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.user.PresenceUpdate;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class PresenceUpdateEvent extends DispatchPayload {
    @JsonProperty("d")
    private PresenceUpdate presenceUpdate;

    public PresenceUpdateEvent() {
    }

    public PresenceUpdateEvent(final Long sequenceNumber, final PresenceUpdate presenceUpdate) {
        super(sequenceNumber, "PRESENCE_UPDATE");
        this.presenceUpdate = presenceUpdate;
    }

    public PresenceUpdate getPresenceUpdate() {
        return presenceUpdate;
    }

    public void setPresenceUpdate(final PresenceUpdate presenceUpdate) {
        this.presenceUpdate = presenceUpdate;
    }
}
