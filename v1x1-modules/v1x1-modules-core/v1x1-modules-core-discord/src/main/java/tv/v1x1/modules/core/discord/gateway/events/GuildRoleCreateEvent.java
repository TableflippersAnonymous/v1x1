package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.permissions.GuildRole;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class GuildRoleCreateEvent extends DispatchPayload {
    @JsonProperty("d")
    private GuildRole guildRole;

    public GuildRoleCreateEvent() {
    }

    public GuildRoleCreateEvent(final Long sequenceNumber, final GuildRole guildRole) {
        super(sequenceNumber, "GUILD_ROLE_CREATE");
        this.guildRole = guildRole;
    }

    public GuildRole getGuildRole() {
        return guildRole;
    }

    public void setGuildRole(final GuildRole guildRole) {
        this.guildRole = guildRole;
    }
}
