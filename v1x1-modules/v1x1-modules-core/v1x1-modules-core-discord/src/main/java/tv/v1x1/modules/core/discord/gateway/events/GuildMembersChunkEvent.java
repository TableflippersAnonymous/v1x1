package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.guild.GuildId;
import tv.v1x1.common.services.discord.dto.guild.GuildMember;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

import java.util.List;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class GuildMembersChunkEvent extends DispatchPayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Chunk extends GuildId {
        @JsonProperty
        private List<GuildMember> members;

        public Chunk() {
        }

        public Chunk(final String guildId, final List<GuildMember> members) {
            super(guildId);
            this.members = members;
        }

        public List<GuildMember> getMembers() {
            return members;
        }

        public void setMembers(final List<GuildMember> members) {
            this.members = members;
        }
    }

    @JsonProperty("d")
    private Chunk chunk;

    public GuildMembersChunkEvent() {
    }

    public GuildMembersChunkEvent(final Long sequenceNumber, final Chunk chunk) {
        super(sequenceNumber, "GUILD_MEMBERS_CHUNK");
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(final Chunk chunk) {
        this.chunk = chunk;
    }
}
