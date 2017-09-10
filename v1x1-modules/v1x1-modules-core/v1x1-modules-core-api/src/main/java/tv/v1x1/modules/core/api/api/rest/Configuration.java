package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 12/31/2016.
 */
public class Configuration {
    @JsonProperty("config_json")
    private String configJson;

    @JsonProperty("enabled")
    private boolean enabled;

    public Configuration() {
    }

    public Configuration(final String configJson, final boolean enabled) {
        this.configJson = configJson;
        this.enabled = enabled;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(final String configJson) {
        this.configJson = configJson;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
