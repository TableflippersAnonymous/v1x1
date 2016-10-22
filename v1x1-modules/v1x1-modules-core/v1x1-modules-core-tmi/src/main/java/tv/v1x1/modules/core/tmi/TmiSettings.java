package tv.v1x1.modules.core.tmi;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.modules.ModuleSettings;

/**
 * Created by cobi on 10/8/2016.
 */
public class TmiSettings extends ModuleSettings {
    private int maxConnections = 20;

    @JsonProperty("max_connections")
    public int getMaxConnections() {
        return maxConnections;
    }

    @JsonProperty("max_connections")
    public void setMaxConnections(final int maxConnections) {
        this.maxConnections = maxConnections;
    }
}
