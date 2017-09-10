package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.guild.VerificationLevel;

/**
 * Created by cobi on 9/16/2017.
 */
public class VerificationLevelAuditLogChange extends AuditLogChange {
    @JsonProperty("old_value")
    private VerificationLevel oldValue;
    @JsonProperty("new_value")
    private VerificationLevel newValue;

    public VerificationLevelAuditLogChange() {
    }

    public VerificationLevelAuditLogChange(final String key, final VerificationLevel oldValue, final VerificationLevel newValue) {
        super(key);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public VerificationLevel getOldValue() {
        return oldValue;
    }

    public void setOldValue(final VerificationLevel oldValue) {
        this.oldValue = oldValue;
    }

    public VerificationLevel getNewValue() {
        return newValue;
    }

    public void setNewValue(final VerificationLevel newValue) {
        this.newValue = newValue;
    }
}
