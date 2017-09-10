package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuildId {
    @JsonProperty("guild_id")
    private String guildId;

    public GuildId() {
    }

    public GuildId(final String guildId) {
        this.guildId = guildId;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(final String guildId) {
        this.guildId = guildId;
    }
}
