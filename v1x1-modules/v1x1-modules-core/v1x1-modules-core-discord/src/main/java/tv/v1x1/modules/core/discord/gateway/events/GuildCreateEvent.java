package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.guild.CompleteGuild;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class GuildCreateEvent extends DispatchPayload {
    @JsonProperty("d")
    private CompleteGuild guild;

    public GuildCreateEvent() {
    }

    public GuildCreateEvent(final Long sequenceNumber, final CompleteGuild guild) {
        super(sequenceNumber, "GUILD_CREATE");
        this.guild = guild;
    }

    public CompleteGuild getGuild() {
        return guild;
    }

    public void setGuild(final CompleteGuild guild) {
        this.guild = guild;
    }
}
