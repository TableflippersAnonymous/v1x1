package tv.twitchbot.modules.core.tmi;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.twitchbot.common.modules.ModuleSettings;

/**
 * Created by naomi on 10/8/2016.
 */
public class TmiSettings extends ModuleSettings {
    private int maxConnections = 800;

    @JsonProperty("max_connections")
    public int getMaxConnections() {
        return maxConnections;
    }

    @JsonProperty("max_connections")
    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }
}
