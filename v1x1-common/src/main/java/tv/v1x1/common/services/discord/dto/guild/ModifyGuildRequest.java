package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/16/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyGuildRequest {
    @JsonProperty
    private String name;
    @JsonProperty
    private String region;
    @JsonProperty("verification_level")
    private VerificationLevel verificationLevel;
    @JsonProperty("default_message_notifications")
    private MessageNotificationLevel defaultMessageNotificationLevel;
    @JsonProperty("afk_channel_id")
    private String afkChannelId;
    @JsonProperty("afk_timeout")
    private Integer afkTimeout;
    @JsonProperty
    private String icon;
    @JsonProperty("owner_id")
    private String ownerId;
    @JsonProperty
    private String splash;

    public ModifyGuildRequest() {
    }

    public ModifyGuildRequest(final String name) {
        this.name = name;
    }

    public ModifyGuildRequest(final String name, final String region) {
        this.name = name;
        this.region = region;
    }

    public ModifyGuildRequest(final VerificationLevel verificationLevel) {
        this.verificationLevel = verificationLevel;
    }

    public ModifyGuildRequest(final MessageNotificationLevel defaultMessageNotificationLevel) {
        this.defaultMessageNotificationLevel = defaultMessageNotificationLevel;
    }

    public ModifyGuildRequest(final String afkChannelId, final Integer afkTimeout) {
        this.afkChannelId = afkChannelId;
        this.afkTimeout = afkTimeout;
    }

    public ModifyGuildRequest(final String name, final String region, final VerificationLevel verificationLevel,
                              final MessageNotificationLevel defaultMessageNotificationLevel, final String afkChannelId,
                              final Integer afkTimeout, final String icon, final String ownerId) {
        this.name = name;
        this.region = region;
        this.verificationLevel = verificationLevel;
        this.defaultMessageNotificationLevel = defaultMessageNotificationLevel;
        this.afkChannelId = afkChannelId;
        this.afkTimeout = afkTimeout;
        this.icon = icon;
        this.ownerId = ownerId;
    }

    public ModifyGuildRequest(final String name, final String region, final VerificationLevel verificationLevel,
                              final MessageNotificationLevel defaultMessageNotificationLevel, final String afkChannelId,
                              final Integer afkTimeout, final String icon, final String ownerId, final String splash) {
        this.name = name;
        this.region = region;
        this.verificationLevel = verificationLevel;
        this.defaultMessageNotificationLevel = defaultMessageNotificationLevel;
        this.afkChannelId = afkChannelId;
        this.afkTimeout = afkTimeout;
        this.icon = icon;
        this.ownerId = ownerId;
        this.splash = splash;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public VerificationLevel getVerificationLevel() {
        return verificationLevel;
    }

    public void setVerificationLevel(final VerificationLevel verificationLevel) {
        this.verificationLevel = verificationLevel;
    }

    public MessageNotificationLevel getDefaultMessageNotificationLevel() {
        return defaultMessageNotificationLevel;
    }

    public void setDefaultMessageNotificationLevel(final MessageNotificationLevel defaultMessageNotificationLevel) {
        this.defaultMessageNotificationLevel = defaultMessageNotificationLevel;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(final String ownerId) {
        this.ownerId = ownerId;
    }

    public String getSplash() {
        return splash;
    }

    public void setSplash(final String splash) {
        this.splash = splash;
    }
}
