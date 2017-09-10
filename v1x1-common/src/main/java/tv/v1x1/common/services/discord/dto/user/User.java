package tv.v1x1.common.services.discord.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @JsonProperty
    private String id;
    @JsonProperty
    private String username;
    @JsonProperty
    private String discriminator;
    @JsonProperty
    private String avatar;
    @JsonProperty
    private boolean bot;
    @JsonProperty("mfa_enabled")
    private boolean mfaEnabled;
    @JsonProperty
    private boolean verified;
    @JsonProperty
    private String email;

    public User() {
    }

    public User(final String id, final String username, final String discriminator, final String avatar,
                final boolean bot, final boolean mfaEnabled, final boolean verified, final String email) {
        this.id = id;
        this.username = username;
        this.discriminator = discriminator;
        this.avatar = avatar;
        this.bot = bot;
        this.mfaEnabled = mfaEnabled;
        this.verified = verified;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(final String discriminator) {
        this.discriminator = discriminator;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(final boolean bot) {
        this.bot = bot;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(final boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(final boolean verified) {
        this.verified = verified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
