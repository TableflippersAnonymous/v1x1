package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.voice.VoiceStateUpdate;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class VoiceStateUpdatePayload extends Payload {
    @JsonProperty("d")
    private VoiceStateUpdate voiceStateUpdate;

    public VoiceStateUpdatePayload() {
    }

    public VoiceStateUpdatePayload(final VoiceStateUpdate voiceStateUpdate) {
        super(4);
        this.voiceStateUpdate = voiceStateUpdate;
    }

    public VoiceStateUpdate getVoiceStateUpdate() {
        return voiceStateUpdate;
    }

    public void setVoiceStateUpdate(final VoiceStateUpdate voiceStateUpdate) {
        this.voiceStateUpdate = voiceStateUpdate;
    }
}
