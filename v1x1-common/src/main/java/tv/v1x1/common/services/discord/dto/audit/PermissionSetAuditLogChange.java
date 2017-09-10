package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.permissions.PermissionSet;

/**
 * Created by cobi on 9/17/2017.
 */
public class PermissionSetAuditLogChange extends AuditLogChange {
    @JsonProperty("old_value")
    private PermissionSet oldValue;
    @JsonProperty("new_value")
    private PermissionSet newValue;

    public PermissionSetAuditLogChange() {
    }

    public PermissionSetAuditLogChange(final String key, final PermissionSet oldValue, final PermissionSet newValue) {
        super(key);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public PermissionSet getOldValue() {
        return oldValue;
    }

    public void setOldValue(final PermissionSet oldValue) {
        this.oldValue = oldValue;
    }

    public PermissionSet getNewValue() {
        return newValue;
    }

    public void setNewValue(final PermissionSet newValue) {
        this.newValue = newValue;
    }
}
