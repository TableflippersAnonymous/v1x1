package tv.v1x1.common.services.discord.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyWebhookRequest {
    @JsonProperty
    private String name;
    @JsonProperty
    private String avatar;

    public ModifyWebhookRequest() {
    }

    public ModifyWebhookRequest(final String name, final String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }
}
