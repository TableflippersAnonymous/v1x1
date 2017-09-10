package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.channel.Overwrite;

import java.util.List;

/**
 * Created by cobi on 9/16/2017.
 */
public class OverwriteListAuditLogChange extends AuditLogChange {
    @JsonProperty("old_value")
    private List<Overwrite> oldValue;
    @JsonProperty("new_value")
    private List<Overwrite> newValue;

    public OverwriteListAuditLogChange() {
    }

    public OverwriteListAuditLogChange(final String key, final List<Overwrite> oldValue, final List<Overwrite> newValue) {
        super(key);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public List<Overwrite> getOldValue() {
        return oldValue;
    }

    public void setOldValue(final List<Overwrite> oldValue) {
        this.oldValue = oldValue;
    }

    public List<Overwrite> getNewValue() {
        return newValue;
    }

    public void setNewValue(final List<Overwrite> newValue) {
        this.newValue = newValue;
    }
}
