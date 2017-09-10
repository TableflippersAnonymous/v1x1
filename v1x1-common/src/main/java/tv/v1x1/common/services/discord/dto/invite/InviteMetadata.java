package tv.v1x1.common.services.discord.dto.invite;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.channel.PartialChannel;
import tv.v1x1.common.services.discord.dto.guild.PartialGuild;
import tv.v1x1.common.services.discord.dto.user.User;

/**
 * Created by naomi on 9/11/2017.
 */
public class InviteMetadata extends Invite {
    @JsonProperty
    private User inviter;
    @JsonProperty
    private Integer uses;
    @JsonProperty("max_uses")
    private Integer maxUses;
    @JsonProperty("max_age")
    private Integer maxAge;
    @JsonProperty
    private boolean temporary;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty
    private boolean revoked;

    public InviteMetadata() {
    }

    public InviteMetadata(final String code, final PartialGuild guild, final PartialChannel channel, final User inviter,
                          final Integer uses, final Integer maxUses, final Integer maxAge, final boolean temporary,
                          final String createdAt, final boolean revoked) {
        super(code, guild, channel);
        this.inviter = inviter;
        this.uses = uses;
        this.maxUses = maxUses;
        this.maxAge = maxAge;
        this.temporary = temporary;
        this.createdAt = createdAt;
        this.revoked = revoked;
    }

    public User getInviter() {
        return inviter;
    }

    public void setInviter(final User inviter) {
        this.inviter = inviter;
    }

    public Integer getUses() {
        return uses;
    }

    public void setUses(final Integer uses) {
        this.uses = uses;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(final Integer maxUses) {
        this.maxUses = maxUses;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(final Integer maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(final boolean temporary) {
        this.temporary = temporary;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(final boolean revoked) {
        this.revoked = revoked;
    }
}
