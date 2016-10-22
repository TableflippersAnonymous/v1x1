package tv.v1x1.modules.core.tmi;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.modules.TenantConfiguration;

/**
 * Created by naomi on 10/8/2016.
 */
public class TmiTenantConfiguration extends TenantConfiguration {
    private boolean customBot = false;
    private String botName;
    private String oauthToken;

    @JsonProperty("custom_bot")
    public boolean isCustomBot() {
        return customBot;
    }

    @JsonProperty("custom_bot")
    public void setCustomBot(final boolean customBot) {
        this.customBot = customBot;
    }

    @JsonProperty("bot_name")
    public String getBotName() {
        return botName;
    }

    @JsonProperty("bot_name")
    public void setBotName(final String botName) {
        this.botName = botName;
    }

    @JsonProperty("oauth_token")
    public String getOauthToken() {
        return oauthToken;
    }

    @JsonProperty("oauth_token")
    public void setOauthToken(final String oauthToken) {
        this.oauthToken = oauthToken;
    }
}
