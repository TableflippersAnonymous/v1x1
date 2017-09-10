package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.permissions.Role;

import java.util.List;

/**
 * Created by cobi on 9/16/2017.
 */
public class RoleListAuditLogChange extends AuditLogChange {
    @JsonProperty("old_value")
    private List<Role> oldValue;
    @JsonProperty("new_value")
    private List<Role> newValue;

    public RoleListAuditLogChange() {
    }

    public RoleListAuditLogChange(final String key, final List<Role> oldValue, final List<Role> newValue) {
        super(key);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public List<Role> getOldValue() {
        return oldValue;
    }

    public void setOldValue(final List<Role> oldValue) {
        this.oldValue = oldValue;
    }

    public List<Role> getNewValue() {
        return newValue;
    }

    public void setNewValue(final List<Role> newValue) {
        this.newValue = newValue;
    }
}
