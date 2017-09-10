package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/16/2017.
 */
public class BooleanAuditLogChange extends AuditLogChange {
    @JsonProperty("old_value")
    private boolean oldValue;
    @JsonProperty("new_value")
    private boolean newValue;

    public BooleanAuditLogChange() {
    }

    public BooleanAuditLogChange(final String key, final boolean oldValue, final boolean newValue) {
        super(key);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public boolean isOldValue() {
        return oldValue;
    }

    public void setOldValue(final boolean oldValue) {
        this.oldValue = oldValue;
    }

    public boolean isNewValue() {
        return newValue;
    }

    public void setNewValue(final boolean newValue) {
        this.newValue = newValue;
    }
}
