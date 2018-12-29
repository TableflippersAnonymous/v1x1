package tv.v1x1.modules.core.tmi;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.modules.GlobalConfiguration;
import tv.v1x1.common.scanners.config.ConfigType;
import tv.v1x1.common.scanners.config.DefaultInteger;
import tv.v1x1.common.scanners.config.DefaultString;
import tv.v1x1.common.scanners.config.Description;
import tv.v1x1.common.scanners.config.DisplayName;
import tv.v1x1.common.scanners.config.ModuleConfig;
import tv.v1x1.common.scanners.config.Type;
import tv.v1x1.common.scanners.config.Version;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
@ModuleConfig("tmi")
@DisplayName("Twitch Global")
@Description("Global configuration for TMI")
@Version(0)
public class TmiGlobalConfiguration extends GlobalConfiguration {
    @DisplayName("Global Bots:")
    @Description("These are the valid global bots and their OAuth tokens")
    @Type(ConfigType.STRING_MAP)
    @JsonProperty("global_bots")
    private Map<String, String> globalBots = new HashMap<>();

    @DisplayName("Connections per Channel:")
    @Description("This is how many connections to TMI per channel are desired.")
    @Type(ConfigType.INTEGER)
    @DefaultInteger(3)
    @JsonProperty("connections_per_channel")
    private int connectionsPerChannel = 3;

    @DisplayName("Default Username:")
    @Description("This is the default username new channels will use.")
    @Type(ConfigType.STRING)
    @DefaultString("v1x1")
    @JsonProperty("default_username")
    private String defaultUsername = "v1x1";


    public Map<String, String> getGlobalBots() {
        return globalBots;
    }

    public void setGlobalBots(final Map<String, String> globalBots) {
        this.globalBots = globalBots;
    }

    public int getConnectionsPerChannel() {
        return connectionsPerChannel;
    }

    public void setConnectionsPerChannel(final int connectionsPerChannel) {
        this.connectionsPerChannel = connectionsPerChannel;
    }

    public String getDefaultUsername() {
        return defaultUsername;
    }

    public void setDefaultUsername(final String defaultUsername) {
        this.defaultUsername = defaultUsername;
    }
}
