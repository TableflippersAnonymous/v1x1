package tv.v1x1.common.services.discord.dto.permissions;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.guild.GuildId;

/**
 * Created by cobi on 9/10/2017.
 */
public class GuildRole extends GuildId {
    @JsonProperty
    private Role role;

    public GuildRole() {
    }

    public GuildRole(final String guildId, final Role role) {
        super(guildId);
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(final Role role) {
        this.role = role;
    }
}

