package tv.v1x1.common.modules;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.scanners.config.ConfigType;
import tv.v1x1.common.scanners.config.Description;
import tv.v1x1.common.scanners.config.DisplayName;
import tv.v1x1.common.scanners.config.Type;

/**
 * Created by naomi on 10/5/16.
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
