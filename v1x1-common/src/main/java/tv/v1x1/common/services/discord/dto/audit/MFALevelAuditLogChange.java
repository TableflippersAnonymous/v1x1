package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.guild.MFALevel;

/**
 * Created by cobi on 9/16/2017.
 */
public class MFALevelAuditLogChange extends AuditLogChange {
    @JsonProperty("old_value")
    private MFALevel oldValue;
    @JsonProperty("new_value")
    private MFALevel newValue;

    public MFALevelAuditLogChange() {
    }

    public MFALevelAuditLogChange(final String key, final MFALevel oldValue, final MFALevel newValue) {
        super(key);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public MFALevel getOldValue() {
        return oldValue;
    }

    public void setOldValue(final MFALevel oldValue) {
        this.oldValue = oldValue;
    }

    public MFALevel getNewValue() {
        return newValue;
    }

    public void setNewValue(final MFALevel newValue) {
        this.newValue = newValue;
    }
}
