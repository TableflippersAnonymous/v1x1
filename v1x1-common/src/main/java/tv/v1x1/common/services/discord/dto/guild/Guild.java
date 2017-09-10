package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.emoji.Emoji;
import tv.v1x1.common.services.discord.dto.permissions.Role;

import java.util.List;

/**
 * Created by naomi on 9/10/2017.
 */
public class Guild extends PartialGuild {
    @JsonProperty("owner_id")
    private String ownerId;
    @JsonProperty("region")
    private String regionId;
    @JsonProperty("afk_channel_id")
    private String afkChannelId;
    @JsonProperty("afk_timeout")
    private Integer afkTimeout;
    @JsonProperty("embed_enabled")
    private boolean embedEnabled;
    @JsonProperty("embed_channel_id")
    private String embedChannelId;
    @JsonProperty("verification_level")
    private VerificationLevel verificationLevel;
    @JsonProperty("default_message_notifications")
    private MessageNotificationLevel defaultMessageNotifications;
    @JsonProperty("explicit_content_filter")
    private ExplicitContentFilterLevel explicitContentFilter;
    @JsonProperty
    private List<Role> roles;
    @JsonProperty
    private List<Emoji> emojis;
    @JsonProperty
    private List<String> features;
    @JsonProperty("mfa_level")
    private MFALevel mfaLevel;
    @JsonProperty("application_id")
    private String applicationId;
    @JsonProperty("widget_enabled")
    private boolean widgetEnabled;
    @JsonProperty("widget_channel_id")
    private String widgetChannelId;

    public Guild() {
    }

    public Guild(final String id, final boolean unavailable, final String name, final String icon, final String splash,
                 final String ownerId, final String regionId, final String afkChannelId, final Integer afkTimeout,
                 final boolean embedEnabled, final String embedChannelId, final VerificationLevel verificationLevel,
                 final MessageNotificationLevel defaultMessageNotifications,
                 final ExplicitContentFilterLevel explicitContentFilter, final List<Role> roles,
                 final List<Emoji> emojis, final List<String> features, final MFALevel mfaLevel,
                 final String applicationId, final boolean widgetEnabled, final String widgetChannelId) {
        super(id, unavailable, name, icon, splash);
        this.ownerId = ownerId;
        this.regionId = regionId;
        this.afkChannelId = afkChannelId;
        this.afkTimeout = afkTimeout;
        this.embedEnabled = embedEnabled;
        this.embedChannelId = embedChannelId;
        this.verificationLevel = verificationLevel;
        this.defaultMessageNotifications = defaultMessageNotifications;
        this.explicitContentFilter = explicitContentFilter;
        this.roles = roles;
        this.emojis = emojis;
        this.features = features;
        this.mfaLevel = mfaLevel;
        this.applicationId = applicationId;
        this.widgetEnabled = widgetEnabled;
        this.widgetChannelId = widgetChannelId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(final String ownerId) {
        this.ownerId = ownerId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(final String regionId) {
        this.regionId = regionId;
    }

    public String getAfkChannelId() {
        return afkChannelId;
    }

    public void setAfkChannelId(final String afkChannelId) {
        this.afkChannelId = afkChannelId;
    }

    public Integer getAfkTimeout() {
        return afkTimeout;
    }

    public void setAfkTimeout(final Integer afkTimeout) {
        this.afkTimeout = afkTimeout;
    }

    public boolean isEmbedEnabled() {
        return embedEnabled;
    }

    public void setEmbedEnabled(final boolean embedEnabled) {
        this.embedEnabled = embedEnabled;
    }

    public String getEmbedChannelId() {
        return embedChannelId;
    }

    public void setEmbedChannelId(final String embedChannelId) {
        this.embedChannelId = embedChannelId;
    }

    public VerificationLevel getVerificationLevel() {
        return verificationLevel;
    }

    public void setVerificationLevel(final VerificationLevel verificationLevel) {
        this.verificationLevel = verificationLevel;
    }

    public MessageNotificationLevel getDefaultMessageNotifications() {
        return defaultMessageNotifications;
    }

    public void setDefaultMessageNotifications(final MessageNotificationLevel defaultMessageNotifications) {
        this.defaultMessageNotifications = defaultMessageNotifications;
    }

    public ExplicitContentFilterLevel getExplicitContentFilter() {
        return explicitContentFilter;
    }

    public void setExplicitContentFilter(final ExplicitContentFilterLevel explicitContentFilter) {
        this.explicitContentFilter = explicitContentFilter;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(final List<Role> roles) {
        this.roles = roles;
    }

    public List<Emoji> getEmojis() {
        return emojis;
    }

    public void setEmojis(final List<Emoji> emojis) {
        this.emojis = emojis;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(final List<String> features) {
        this.features = features;
    }

    public MFALevel getMfaLevel() {
        return mfaLevel;
    }

    public void setMfaLevel(final MFALevel mfaLevel) {
        this.mfaLevel = mfaLevel;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(final String applicationId) {
        this.applicationId = applicationId;
    }

    public boolean isWidgetEnabled() {
        return widgetEnabled;
    }

    public void setWidgetEnabled(final boolean widgetEnabled) {
        this.widgetEnabled = widgetEnabled;
    }

    public String getWidgetChannelId() {
        return widgetChannelId;
    }

    public void setWidgetChannelId(final String widgetChannelId) {
        this.widgetChannelId = widgetChannelId;
    }
}
