package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by cobi on 9/13/2017.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "key",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "name"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "icon_hash"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "splash_hash"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "owner_id"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "region"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "afk_channel_id"),
        @JsonSubTypes.Type(value = IntegerAuditLogChange.class, name = "afk_timeout"),
        @JsonSubTypes.Type(value = MFALevelAuditLogChange.class, name = "mfa_level"),
        @JsonSubTypes.Type(value = VerificationLevelAuditLogChange.class, name = "verification_level"),
        @JsonSubTypes.Type(value = ExplicitContentFilterLevelAuditLogChange.class, name = "explicit_content_filter"),
        @JsonSubTypes.Type(value = MessageNotificationLevelAuditLogChange.class, name = "default_message_notifications"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "vanity_url_code"),
        @JsonSubTypes.Type(value = RoleListAuditLogChange.class, name = "$add"),
        @JsonSubTypes.Type(value = RoleListAuditLogChange.class, name = "$remove"),
        @JsonSubTypes.Type(value = IntegerAuditLogChange.class, name = "prune_delete_days"),
        @JsonSubTypes.Type(value = BooleanAuditLogChange.class, name = "widget_enabled"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "widget_channel_id"),
        @JsonSubTypes.Type(value = IntegerAuditLogChange.class, name = "position"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "topic"),
        @JsonSubTypes.Type(value = IntegerAuditLogChange.class, name = "bitrate"),
        @JsonSubTypes.Type(value = OverwriteListAuditLogChange.class, name = "permission_overwrites"),
        @JsonSubTypes.Type(value = BooleanAuditLogChange.class, name = "nsfw"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "application_id"),
        @JsonSubTypes.Type(value = PermissionSetAuditLogChange.class, name = "permissions"),
        @JsonSubTypes.Type(value = IntegerAuditLogChange.class, name = "color"),
        @JsonSubTypes.Type(value = BooleanAuditLogChange.class, name = "hoist"),
        @JsonSubTypes.Type(value = BooleanAuditLogChange.class, name = "mentionable"),
        @JsonSubTypes.Type(value = PermissionSetAuditLogChange.class, name = "allow"),
        @JsonSubTypes.Type(value = PermissionSetAuditLogChange.class, name = "deny"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "code"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "channel_id"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "inviter_id"),
        @JsonSubTypes.Type(value = IntegerAuditLogChange.class, name = "max_uses"),
        @JsonSubTypes.Type(value = IntegerAuditLogChange.class, name = "uses"),
        @JsonSubTypes.Type(value = IntegerAuditLogChange.class, name = "max_age"),
        @JsonSubTypes.Type(value = BooleanAuditLogChange.class, name = "temporary"),
        @JsonSubTypes.Type(value = BooleanAuditLogChange.class, name = "deaf"),
        @JsonSubTypes.Type(value = BooleanAuditLogChange.class, name = "mute"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "nick"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "avatar_hash"),
        @JsonSubTypes.Type(value = StringAuditLogChange.class, name = "id"),
        @JsonSubTypes.Type(value = StringOrIntegerAuditLogChange.class, name = "type"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AuditLogChange {
    @JsonProperty
    private String key;

    public AuditLogChange() {
    }

    public AuditLogChange(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }
}
