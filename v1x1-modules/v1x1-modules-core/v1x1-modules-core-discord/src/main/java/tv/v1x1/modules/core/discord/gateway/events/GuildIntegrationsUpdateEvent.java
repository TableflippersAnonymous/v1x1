package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.guild.GuildId;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class GuildIntegrationsUpdateEvent extends DispatchPayload {
    @JsonProperty("d")
    private GuildId guildId;

    public GuildIntegrationsUpdateEvent() {
    }

    public GuildIntegrationsUpdateEvent(final Long sequenceNumber, final GuildId guildId) {
        super(sequenceNumber, "GUILD_INTEGRATIONS_UPDATE");
        this.guildId = guildId;
    }

    public GuildId getGuildId() {
        return guildId;
    }

    public void setGuildId(final GuildId guildId) {
        this.guildId = guildId;
    }
}
