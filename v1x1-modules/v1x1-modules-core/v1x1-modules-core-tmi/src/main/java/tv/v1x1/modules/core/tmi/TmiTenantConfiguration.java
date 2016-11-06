package tv.v1x1.modules.core.tmi;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.config.ConfigType;
import tv.v1x1.common.config.Description;
import tv.v1x1.common.config.DisplayName;
import tv.v1x1.common.config.ModuleConfig;
import tv.v1x1.common.config.Requires;
import tv.v1x1.common.config.Type;
import tv.v1x1.common.config.Version;
import tv.v1x1.common.modules.TenantConfiguration;

/**
 * Created by cobi on 10/8/2016.
 */
@ModuleConfig("tmi")
@DisplayName("Twitch")
@Description("This module controls settings used when connecting to your Twitch Channel.")
@Version(0)
public class TmiTenantConfiguration implements TenantConfiguration {
    @DisplayName("Custom Bot?")
    @Description("This allows you to enter your own bot credentials to use.")
    @Type(ConfigType.BOOLEAN)
    @JsonProperty("custom_bot")
    private boolean customBot = false;

    @DisplayName("Bot name:")
    @Description("This allows you to enter a bot name other than v1x1.")
    @Type(ConfigType.BOT_NAME)
    @JsonProperty("bot_name")
    private String botName;

    @DisplayName("OAuth Token:")
    @Description("What is your Twitch OAuth token for the bot?")
    @Type(ConfigType.TWITCH_OAUTH)
    @Requires("custom_bot")
    @JsonProperty("oauth_token")
    private String oauthToken;


    public boolean isCustomBot() {
        return customBot;
    }

    public void setCustomBot(final boolean customBot) {
        this.customBot = customBot;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(final String botName) {
        this.botName = botName;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(final String oauthToken) {
        this.oauthToken = oauthToken;
    }
}
