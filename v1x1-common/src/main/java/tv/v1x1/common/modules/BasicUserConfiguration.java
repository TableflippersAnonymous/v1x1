package tv.v1x1.common.modules;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.config.ConfigType;
import tv.v1x1.common.config.Description;
import tv.v1x1.common.config.DisplayName;
import tv.v1x1.common.config.Type;

/**
 * Created by cobi on 10/5/16.
 */
public class BasicUserConfiguration implements UserConfiguration {
    @DisplayName("Enabled?")
    @Description("This controls whether this module is enabled on your channel.")
    @Type(ConfigType.MASTER_ENABLE)
    @JsonProperty
    private boolean enabled;
    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
