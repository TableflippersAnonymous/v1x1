package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.voice.VoiceState;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class VoiceStateUpdateEvent extends DispatchPayload {
    @JsonProperty("d")
    private VoiceState voiceState;

    public VoiceStateUpdateEvent() {
    }

    public VoiceStateUpdateEvent(final Long sequenceNumber, final VoiceState voiceState) {
        super(sequenceNumber, "VOICE_STATE_UPDATE");
        this.voiceState = voiceState;
    }

    public VoiceState getVoiceState() {
        return voiceState;
    }

    public void setVoiceState(final VoiceState voiceState) {
        this.voiceState = voiceState;
    }
}
