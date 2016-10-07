package tv.twitchbot.common.modules;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/5/16.
 */
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
