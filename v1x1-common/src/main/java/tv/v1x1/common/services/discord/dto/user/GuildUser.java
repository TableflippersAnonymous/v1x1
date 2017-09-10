package tv.v1x1.common.services.discord.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/10/2017.
 */
public class GuildUser extends User {
    @JsonProperty("guild_id")
    private String guildId;

    public GuildUser() {
    }

    public GuildUser(final String id, final String username, final String discriminator, final String avatar,
                     final boolean bot, final boolean mfaEnabled, final boolean verified, final String email,
                     final String guildId) {
        super(id, username, discriminator, avatar, bot, mfaEnabled, verified, email);
        this.guildId = guildId;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(final String guildId) {
        this.guildId = guildId;
    }
}
