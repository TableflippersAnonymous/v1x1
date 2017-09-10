package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.channel.PartialChannel;
import tv.v1x1.common.services.discord.dto.permissions.Role;

import java.util.List;

/**
 * Created by cobi on 9/16/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateGuildRequest {
    @JsonProperty
    private String name;
    @JsonProperty
    private String region;
    @JsonProperty
    private String icon;
    @JsonProperty("verification_level")
    private VerificationLevel verificationLevel;
    @JsonProperty("default_message_notifications")
    private MessageNotificationLevel defaultMessageNotificationLevel;
    @JsonProperty
    private List<Role> roles;
    @JsonProperty
    private List<PartialChannel> channels;

    public CreateGuildRequest() {
    }

    public CreateGuildRequest(final String name, final String region, final String icon,
                              final VerificationLevel verificationLevel,
                              final MessageNotificationLevel defaultMessageNotificationLevel, final List<Role> roles,
                              final List<PartialChannel> channels) {
        this.name = name;
        this.region = region;
        this.icon = icon;
        this.verificationLevel = verificationLevel;
        this.defaultMessageNotificationLevel = defaultMessageNotificationLevel;
        this.roles = roles;
        this.channels = channels;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(final List<Role> roles) {
        this.roles = roles;
    }

    public List<PartialChannel> getChannels() {
        return channels;
    }

    public void setChannels(final List<PartialChannel> channels) {
        this.channels = channels;
    }
}
