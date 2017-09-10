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
public class GuildRoleDeleteEvent extends DispatchPayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GuildRoleId extends GuildId {
        @JsonProperty("role_id")
        private String roleId;

        public GuildRoleId() {
        }

        public GuildRoleId(final String guildId, final String roleId) {
            super(guildId);
            this.roleId = roleId;
        }

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(final String roleId) {
            this.roleId = roleId;
        }
    }

    @JsonProperty("d")
    private GuildRoleId guildRoleId;

    public GuildRoleDeleteEvent() {
    }

    public GuildRoleDeleteEvent(final Long sequenceNumber, final GuildRoleId guildRoleId) {
        super(sequenceNumber, "GUILD_ROLE_DELETE");
        this.guildRoleId = guildRoleId;
    }

    public GuildRoleId getGuildRoleId() {
        return guildRoleId;
    }

    public void setGuildRoleId(final GuildRoleId guildRoleId) {
        this.guildRoleId = guildRoleId;
    }
}
