package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.guild.GuildId;
import tv.v1x1.common.services.discord.dto.user.User;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class GuildMemberRemoveEvent extends DispatchPayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data extends GuildId {
        @JsonProperty
        private User user;

        public Data() {
        }

        public Data(final String guildId, final User user) {
            super(guildId);
            this.user = user;
        }

        public User getUser() {
            return user;
        }

        public void setUser(final User user) {
            this.user = user;
        }
    }

    @JsonProperty("d")
    private Data data;

    public GuildMemberRemoveEvent() {
    }

    public GuildMemberRemoveEvent(final Long sequenceNumber, final Data data) {
        super(sequenceNumber, "GUILD_MEMBER_REMOVE");
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(final Data data) {
        this.data = data;
    }
}
