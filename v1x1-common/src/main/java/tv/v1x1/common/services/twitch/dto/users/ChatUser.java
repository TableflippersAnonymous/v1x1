package tv.v1x1.common.services.twitch.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatUser {
    private static final int RATE_LIMIT_BUFFER = 2;
    private static final int RATE_LIMIT_VERIFIED = 7500;
    private static final int RATE_LIMIT_KNOWN = 50;
    private static final int RATE_LIMIT_USER = 20;

    @JsonProperty("_id")
    private String id;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty
    private String login;
    @JsonProperty
    private String color;
    @JsonProperty("is_verified_bot")
    private boolean verifiedBot;
    @JsonProperty("is_known_bot")
    private boolean knownBot;
    @JsonProperty
    private List<ChatBadge> badges;

    public ChatUser() {
    }

    public ChatUser(final String id, final String displayName, final String login, final String color,
                    final boolean verifiedBot, final boolean knownBot, final List<ChatBadge> badges) {
        this.id = id;
        this.displayName = displayName;
        this.login = login;
        this.color = color;
        this.verifiedBot = verifiedBot;
        this.knownBot = knownBot;
        this.badges = badges;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public boolean isVerifiedBot() {
        return verifiedBot;
    }

    public void setVerifiedBot(final boolean verifiedBot) {
        this.verifiedBot = verifiedBot;
    }

    public boolean isKnownBot() {
        return knownBot;
    }

    public void setKnownBot(final boolean knownBot) {
        this.knownBot = knownBot;
    }

    public List<ChatBadge> getBadges() {
        return badges;
    }

    public void setBadges(final List<ChatBadge> badges) {
        this.badges = badges;
    }

    /* per 30s */
    public int getChatRateLimit() {
        if(isVerifiedBot())
            return RATE_LIMIT_VERIFIED - RATE_LIMIT_BUFFER;
        if(isKnownBot())
            return RATE_LIMIT_KNOWN - RATE_LIMIT_BUFFER;
        return RATE_LIMIT_USER - RATE_LIMIT_BUFFER;
    }
}
