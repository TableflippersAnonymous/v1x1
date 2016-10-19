package tv.twitchbot.common.modules;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 10/5/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TenantConfiguration {
    private boolean enabled;

    @JsonProperty
    public boolean isEnabled() {
        return enabled;
    }

    @JsonProperty
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
