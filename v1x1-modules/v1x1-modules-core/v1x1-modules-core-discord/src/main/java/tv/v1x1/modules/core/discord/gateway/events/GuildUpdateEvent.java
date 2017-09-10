package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.guild.Guild;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class GuildUpdateEvent extends DispatchPayload {
    @JsonProperty("d")
    private Guild guild;

    public GuildUpdateEvent() {
    }

    public GuildUpdateEvent(final Long sequenceNumber, final Guild guild) {
        super(sequenceNumber, "GUILD_UPDATE");
        this.guild = guild;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(final Guild guild) {
        this.guild = guild;
    }
}
