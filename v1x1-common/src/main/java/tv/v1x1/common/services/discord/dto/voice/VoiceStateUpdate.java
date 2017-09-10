package tv.v1x1.common.services.discord.dto.voice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoiceStateUpdate {
    @JsonProperty("guild_id")
    private String guildId;
    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty("self_mute")
    private boolean selfMute;
    @JsonProperty("self_deaf")
    private boolean selfDeaf;

    public VoiceStateUpdate() {
    }

    public VoiceStateUpdate(final String guildId, final String channelId, final boolean selfMute, final boolean selfDeaf) {
        this.guildId = guildId;
        this.channelId = channelId;
        this.selfMute = selfMute;
        this.selfDeaf = selfDeaf;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(final String guildId) {
        this.guildId = guildId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public boolean isSelfMute() {
        return selfMute;
    }

    public void setSelfMute(final boolean selfMute) {
        this.selfMute = selfMute;
    }

    public boolean isSelfDeaf() {
        return selfDeaf;
    }

    public void setSelfDeaf(final boolean selfDeaf) {
        this.selfDeaf = selfDeaf;
    }
}
