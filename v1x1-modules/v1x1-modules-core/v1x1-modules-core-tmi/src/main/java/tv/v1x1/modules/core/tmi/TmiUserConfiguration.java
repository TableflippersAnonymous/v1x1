package tv.v1x1.modules.core.tmi;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.modules.UserConfiguration;
import tv.v1x1.common.scanners.config.ConfigType;
import tv.v1x1.common.scanners.config.Description;
import tv.v1x1.common.scanners.config.DisplayName;
import tv.v1x1.common.scanners.config.ModuleConfig;
import tv.v1x1.common.scanners.config.Permission;
import tv.v1x1.common.scanners.config.Requires;
import tv.v1x1.common.scanners.config.TenantPermission;
import tv.v1x1.common.scanners.config.Type;
import tv.v1x1.common.scanners.config.Version;

/**
 * Created by naomi on 10/8/2016.
 */
@ModuleConfig("tmi")
@DisplayName("Twitch")
@Description("This module controls settings used when connecting to your Twitch Channel.")
@Version(1)
public class TmiUserConfiguration implements UserConfiguration {
    @DisplayName("Custom Bot?")
    @Description("This allows you to enter your own bot credentials to use.")
    @Type(ConfigType.BOOLEAN)
    @JsonProperty("custom_bot")
    @TenantPermission(Permission.READ_WRITE)
    private boolean customBot = false;

    @DisplayName("Bot name")
    @Description("This allows you to enter a bot name other than v1x1.")
    @Type(ConfigType.BOT_NAME)
    @JsonProperty("bot_name")
    @TenantPermission(Permission.NONE)
    private String botName;

    @DisplayName("Bot Account")
    @Description("TMI")
    @Type(ConfigType.OAUTH)
    @Requires("custom_bot")
    @JsonProperty("oauth_token")
    @TenantPermission(Permission.WRITE_ONLY)
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
