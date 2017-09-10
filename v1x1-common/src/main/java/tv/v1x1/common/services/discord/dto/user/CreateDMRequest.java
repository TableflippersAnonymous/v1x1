package tv.v1x1.common.services.discord.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateDMRequest {
    @JsonProperty("recipient_id")
    private String recipientId;

    public CreateDMRequest() {
    }

    public CreateDMRequest(final String recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(final String recipientId) {
        this.recipientId = recipientId;
    }
}
