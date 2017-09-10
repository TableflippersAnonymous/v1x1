package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.guild.GuildMemberWithGuildId;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class GuildMemberAddEvent extends DispatchPayload {
    @JsonProperty("d")
    private GuildMemberWithGuildId memberWithGuildId;

    public GuildMemberAddEvent() {
    }

    public GuildMemberAddEvent(final Long sequenceNumber, final GuildMemberWithGuildId memberWithGuildId) {
        super(sequenceNumber, "GUILD_MEMBER_ADD");
        this.memberWithGuildId = memberWithGuildId;
    }

    public GuildMemberWithGuildId getMemberWithGuildId() {
        return memberWithGuildId;
    }

    public void setMemberWithGuildId(final GuildMemberWithGuildId memberWithGuildId) {
        this.memberWithGuildId = memberWithGuildId;
    }
}
