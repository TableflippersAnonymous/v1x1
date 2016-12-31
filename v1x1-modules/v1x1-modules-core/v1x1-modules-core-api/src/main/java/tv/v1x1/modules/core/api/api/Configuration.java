package tv.v1x1.modules.core.api.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 12/31/2016.
 */
public class Configuration {
    @JsonProperty("config_json")
    private String configJson;

    public Configuration() {
    }

    public Configuration(final String configJson) {
        this.configJson = configJson;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(final String configJson) {
        this.configJson = configJson;
    }
}
