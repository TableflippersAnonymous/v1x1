package tv.v1x1.common.services.discord.dto.voice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Created by naomi on 9/10/2017.
 */
public class VoiceState extends PartialVoiceState {
    @JsonProperty("guild_id")
    private String guildId;

    public static VoiceState fromProto(final EventOuterClass.DiscordVoiceStateEvent.VoiceState proto) {
        return new VoiceState(
                proto.hasChannelId() ? proto.getChannelId() : null,
                proto.getUserId(),
                proto.getSessionId(),
                proto.getDeaf(),
                proto.getMute(),
                proto.getSelfDeaf(),
                proto.getSelfMute(),
                proto.getSuppress(),
                proto.hasGuildId() ? proto.getGuildId() : null
        );
    }

    public VoiceState() {
    }

    public VoiceState(final String channelId, final String userId, final String sessionId, final boolean deaf, final boolean mute, final boolean selfDeaf, final boolean selfMute, final boolean suppress, final String guildId) {
        super(channelId, userId, sessionId, deaf, mute, selfDeaf, selfMute, suppress);
        this.guildId = guildId;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(final String guildId) {
        this.guildId = guildId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final VoiceState that = (VoiceState) o;
        return Objects.equal(guildId, that.guildId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), guildId);
    }

    public EventOuterClass.DiscordVoiceStateEvent.VoiceState toProto() {
        EventOuterClass.DiscordVoiceStateEvent.VoiceState.Builder builder = EventOuterClass.DiscordVoiceStateEvent.VoiceState.newBuilder()
                .setUserId(getUserId())
                .setSessionId(getSessionId())
                .setDeaf(isDeaf())
                .setMute(isMute())
                .setSelfDeaf(isSelfDeaf())
                .setSelfMute(isSelfMute())
                .setSuppress(isSuppress());
        if(getChannelId() != null)
            builder = builder.setChannelId(getChannelId());
        if(getGuildId() != null)
            builder = builder.setGuildId(getGuildId());
        return builder.build();
    }

    @Override
    public String toString() {
        final ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
