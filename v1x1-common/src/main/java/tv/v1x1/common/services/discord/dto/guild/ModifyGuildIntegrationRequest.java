package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyGuildIntegrationRequest {
    @JsonProperty("expire_behavior")
    private Integer expireBehavior;
    @JsonProperty("expire_grace_period")
    private Integer expireGracePeriod;
    @JsonProperty("enable_emoticons")
    private Boolean enableEmoticons;

    public ModifyGuildIntegrationRequest() {
    }

    public ModifyGuildIntegrationRequest(final Integer expireBehavior, final Integer expireGracePeriod,
                                         final Boolean enableEmoticons) {
        this.expireBehavior = expireBehavior;
        this.expireGracePeriod = expireGracePeriod;
        this.enableEmoticons = enableEmoticons;
    }

    public Integer getExpireBehavior() {
        return expireBehavior;
    }

    public void setExpireBehavior(final Integer expireBehavior) {
        this.expireBehavior = expireBehavior;
    }

    public Integer getExpireGracePeriod() {
        return expireGracePeriod;
    }

    public void setExpireGracePeriod(final Integer expireGracePeriod) {
        this.expireGracePeriod = expireGracePeriod;
    }

    public Boolean getEnableEmoticons() {
        return enableEmoticons;
    }

    public void setEnableEmoticons(final Boolean enableEmoticons) {
        this.enableEmoticons = enableEmoticons;
    }
}
