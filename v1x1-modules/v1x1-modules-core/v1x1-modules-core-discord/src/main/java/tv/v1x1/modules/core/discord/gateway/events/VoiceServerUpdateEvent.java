package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.guild.GuildId;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class VoiceServerUpdateEvent extends DispatchPayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VoiceServerUpdate extends GuildId {
        @JsonProperty
        private String token;
        @JsonProperty
        private String endpoint;

        public VoiceServerUpdate() {
        }

        public VoiceServerUpdate(final String guildId, final String token, final String endpoint) {
            super(guildId);
            this.token = token;
            this.endpoint = endpoint;
        }

        public String getToken() {
            return token;
        }

        public void setToken(final String token) {
            this.token = token;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(final String endpoint) {
            this.endpoint = endpoint;
        }
    }

    @JsonProperty("d")
    private VoiceServerUpdate voiceServerUpdate;

    public VoiceServerUpdateEvent() {
    }

    public VoiceServerUpdateEvent(final Long sequenceNumber, final VoiceServerUpdate voiceServerUpdate) {
        super(sequenceNumber, "VOICE_SERVER_UPDATE");
        this.voiceServerUpdate = voiceServerUpdate;
    }

    public VoiceServerUpdate getVoiceServerUpdate() {
        return voiceServerUpdate;
    }

    public void setVoiceServerUpdate(final VoiceServerUpdate voiceServerUpdate) {
        this.voiceServerUpdate = voiceServerUpdate;
    }
}
