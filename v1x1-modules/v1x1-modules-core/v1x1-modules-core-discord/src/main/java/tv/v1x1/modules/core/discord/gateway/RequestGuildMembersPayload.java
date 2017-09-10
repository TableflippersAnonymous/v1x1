package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.guild.GuildId;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class RequestGuildMembersPayload extends Payload {
    public static class GuildMembersRequest extends GuildId {
        @JsonProperty
        private String query;
        @JsonProperty
        private Integer limit;

        public GuildMembersRequest() {
        }

        public GuildMembersRequest(final String guildId, final String query, final Integer limit) {
            super(guildId);
            this.query = query;
            this.limit = limit;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(final String query) {
            this.query = query;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(final Integer limit) {
            this.limit = limit;
        }
    }

    @JsonProperty("d")
    private GuildMembersRequest guildMembersRequest;

    public RequestGuildMembersPayload() {
    }

    public RequestGuildMembersPayload(final GuildMembersRequest guildMembersRequest) {
        super(8);
        this.guildMembersRequest = guildMembersRequest;
    }

    public GuildMembersRequest getGuildMembersRequest() {
        return guildMembersRequest;
    }

    public void setGuildMembersRequest(final GuildMembersRequest guildMembersRequest) {
        this.guildMembersRequest = guildMembersRequest;
    }
}
