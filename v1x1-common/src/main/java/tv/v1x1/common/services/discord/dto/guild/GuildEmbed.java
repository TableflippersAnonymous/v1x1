package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuildEmbed {
    @JsonProperty
    private Boolean enabled;
    @JsonProperty("channel_id")
    private String channelId;

    public GuildEmbed() {
    }

    public GuildEmbed(final Boolean enabled) {
        this.enabled = enabled;
    }

    public GuildEmbed(final String channelId) {
        this.channelId = channelId;
    }

    public GuildEmbed(final Boolean enabled, final String channelId) {
        this.enabled = enabled;
        this.channelId = channelId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }
}
