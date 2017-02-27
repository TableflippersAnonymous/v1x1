package tv.v1x1.common.services.twitch.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 2/28/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VhsId {
    @JsonProperty
    private String identifier;

    public VhsId() {
    }

    public VhsId(final String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }
}
