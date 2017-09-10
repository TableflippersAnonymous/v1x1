package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Created by naomi on 9/11/2017.
 */
public enum AuditLogEventType {
    GUILD_UPDATE(1),
    CHANNEL_CREATE(10), CHANNEL_UPDATE(11), CHANNEL_DELETE(12),
    CHANNEL_OVERWRITE_CREATE(13), CHANNEL_OVERWRITE_UPDATE(14), CHANNEL_OVERWRITE_DELETE(15),
    MEMBER_KICK(20), MEMBER_PRUNE(21), MEMBER_BAN_ADD(22), MEMBER_BAN_REMOVE(23),
    MEMBER_UPDATE(24), MEMBER_ROLE_UPDATE(25),
    ROLE_CREATE(30), ROLE_UPDATE(31), ROLE_DELETE(32),
    INVITE_CREATE(40), INVITE_UPDATE(41), INVITE_DELETE(42),
    WEBHOOK_CREATE(50), WEBHOOK_UPDATE(51), WEBHOOK_DELETE(52),
    EMOJI_CREATE(60), EMOJI_UPDATE(61), EMOJI_DELETE(62),
    MESSAGE_DELETE(72);

    private final int value;

    AuditLogEventType(final int value) {
        this.value = value;
    }

    @JsonValue
    public Integer jsonValue() {
        return this.value;
    }

    @JsonCreator
    public static AuditLogEventType fromJson(final Integer value) {
        for(final AuditLogEventType auditLogEventType : values())
            if(Objects.equals(auditLogEventType.jsonValue(), value))
                return auditLogEventType;
        throw new IllegalArgumentException("value=" + value + " not recognized");
    }

    public int getValue() {
        return value;
    }
}
