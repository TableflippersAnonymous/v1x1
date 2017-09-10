package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.guild.UnavailableGuild;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class GuildDeleteEvent extends DispatchPayload {
    @JsonProperty("d")
    private UnavailableGuild guild;

    public GuildDeleteEvent() {
    }

    public GuildDeleteEvent(final Long sequenceNumber, final UnavailableGuild guild) {
        super(sequenceNumber, "GUILD_DELETE");
        this.guild = guild;
    }

    public UnavailableGuild getGuild() {
        return guild;
    }

    public void setGuild(final UnavailableGuild guild) {
        this.guild = guild;
    }
}
