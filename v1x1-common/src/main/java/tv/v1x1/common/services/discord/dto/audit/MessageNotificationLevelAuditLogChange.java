package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.guild.MessageNotificationLevel;

/**
 * Created by naomi on 9/16/2017.
 */
public class MessageNotificationLevelAuditLogChange extends AuditLogChange {
    @JsonProperty("old_value")
    private MessageNotificationLevel oldValue;
    @JsonProperty("new_value")
    private MessageNotificationLevel newValue;

    public MessageNotificationLevelAuditLogChange() {
    }

    public MessageNotificationLevelAuditLogChange(final String key, final MessageNotificationLevel oldValue, final MessageNotificationLevel newValue) {
        super(key);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public MessageNotificationLevel getOldValue() {
        return oldValue;
    }

    public void setOldValue(final MessageNotificationLevel oldValue) {
        this.oldValue = oldValue;
    }

    public MessageNotificationLevel getNewValue() {
        return newValue;
    }

    public void setNewValue(final MessageNotificationLevel newValue) {
        this.newValue = newValue;
    }
}
