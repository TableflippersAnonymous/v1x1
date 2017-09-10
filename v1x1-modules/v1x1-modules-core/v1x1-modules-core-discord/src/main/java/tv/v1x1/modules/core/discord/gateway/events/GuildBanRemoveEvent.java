package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.user.GuildUser;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class GuildBanRemoveEvent extends DispatchPayload {
    @JsonProperty("d")
    private GuildUser user;

    public GuildBanRemoveEvent() {
    }

    public GuildBanRemoveEvent(final Long sequenceNumber, final GuildUser user) {
        super(sequenceNumber, "GUILD_BAN_REMOVE");
        this.user = user;
    }

    public GuildUser getUser() {
        return user;
    }

    public void setUser(final GuildUser user) {
        this.user = user;
    }
}
