package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.permissions.GuildRole;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class GuildRoleUpdateEvent extends DispatchPayload {
    @JsonProperty("d")
    private GuildRole guildRole;

    public GuildRoleUpdateEvent() {
    }

    public GuildRoleUpdateEvent(final Long sequenceNumber, final GuildRole guildRole) {
        super(sequenceNumber, "GUILD_ROLE_UPDATE");
        this.guildRole = guildRole;
    }

    public GuildRole getGuildRole() {
        return guildRole;
    }

    public void setGuildRole(final GuildRole guildRole) {
        this.guildRole = guildRole;
    }
}
