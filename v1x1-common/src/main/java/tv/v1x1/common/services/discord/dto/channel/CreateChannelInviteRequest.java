package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/11/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateChannelInviteRequest {
    @JsonProperty("max_age")
    private Integer maxAge;
    @JsonProperty("max_uses")
    private Integer maxUses;
    @JsonProperty
    private boolean temporary;
    @JsonProperty
    private boolean unique;

    public CreateChannelInviteRequest() {
    }

    public CreateChannelInviteRequest(final Integer maxAge, final Integer maxUses, final boolean temporary, final boolean unique) {
        this.maxAge = maxAge;
        this.maxUses = maxUses;
        this.temporary = temporary;
        this.unique = unique;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(final Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(final Integer maxUses) {
        this.maxUses = maxUses;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(final boolean temporary) {
        this.temporary = temporary;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(final boolean unique) {
        this.unique = unique;
    }
}
