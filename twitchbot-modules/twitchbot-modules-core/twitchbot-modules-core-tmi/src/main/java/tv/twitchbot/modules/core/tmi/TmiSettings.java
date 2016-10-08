package tv.twitchbot.modules.core.tmi;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.twitchbot.common.modules.ModuleSettings;

/**
 * Created by naomi on 10/8/2016.
 */
public class TmiSettings extends ModuleSettings {
    private String oauthToken;
    private String userName;
    private int connections;
    private int channelsPerConnection;

    @JsonProperty("oauth_token")
    public String getOauthToken() {
        return oauthToken;
    }

    @JsonProperty("oauth_token")
    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    @JsonProperty("username")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("username")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("connections")
    public int getConnections() {
        return connections;
    }

    @JsonProperty("connections")
    public void setConnections(int connections) {
        this.connections = connections;
    }

    @JsonProperty("channels_per_connection")
    public int getChannelsPerConnection() {
        return channelsPerConnection;
    }

    @JsonProperty("channels_per_connection")
    public void setChannelsPerConnection(int channelsPerConnection) {
        this.channelsPerConnection = channelsPerConnection;
    }
}
