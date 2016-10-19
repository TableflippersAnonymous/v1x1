package tv.twitchbot.modules.core.tmi;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.twitchbot.common.modules.GlobalConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public class TmiGlobalConfiguration extends GlobalConfiguration {
    private Map<String, String> globalBots = new HashMap<>();
    private int connectionsPerChannel = 3;
    private String defaultUsername = "v1x1";

    @JsonProperty("global_bots")
    public Map<String, String> getGlobalBots() {
        return globalBots;
    }

    @JsonProperty("global_bots")
    public void setGlobalBots(Map<String, String> globalBots) {
        this.globalBots = globalBots;
    }

    @JsonProperty("connections_per_channel")
    public int getConnectionsPerChannel() {
        return connectionsPerChannel;
    }

    @JsonProperty("connections_per_channel")
    public void setConnectionsPerChannel(int connectionsPerChannel) {
        this.connectionsPerChannel = connectionsPerChannel;
    }

    @JsonProperty("default_username")
    public String getDefaultUsername() {
        return defaultUsername;
    }

    @JsonProperty("default_username")
    public void setDefaultUsername(String defaultUsername) {
        this.defaultUsername = defaultUsername;
    }
}
