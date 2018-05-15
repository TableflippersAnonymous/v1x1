package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.guild.GuildId;
import tv.v1x1.common.services.discord.dto.user.User;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

import java.util.List;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class GuildMemberUpdateEvent extends DispatchPayload {
    @JsonProperty("d")
    private Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data extends GuildId {
        @JsonProperty("roles")
        private List<String> roleIds;
        @JsonProperty
        private User user;
        @JsonProperty
        private String nick;

        public Data() {
        }

        public Data(final String guildId, final List<String> roleIds, final User user, final String nick) {
            super(guildId);
            this.roleIds = roleIds;
            this.user = user;
            this.nick = nick;
        }

        public List<String> getRoleIds() {
            return roleIds;
        }

        public void setRoleIds(final List<String> roleIds) {
            this.roleIds = roleIds;
        }

        public User getUser() {
            return user;
        }

        public void setUser(final User user) {
            this.user = user;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(final String nick) {
            this.nick = nick;
        }
    }

    public GuildMemberUpdateEvent() {
    }

    public GuildMemberUpdateEvent(final Long sequenceNumber, final Data data) {
        super(sequenceNumber, "GUILD_MEMBER_UPDATE");
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(final Data data) {
        this.data = data;
    }
}
