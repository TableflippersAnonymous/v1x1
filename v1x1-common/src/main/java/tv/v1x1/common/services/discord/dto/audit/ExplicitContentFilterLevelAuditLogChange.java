package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.guild.ExplicitContentFilterLevel;

/**
 * Created by cobi on 9/16/2017.
 */
public class ExplicitContentFilterLevelAuditLogChange extends AuditLogChange {
    @JsonProperty("old_value")
    private ExplicitContentFilterLevel oldValue;
    @JsonProperty("new_value")
    private ExplicitContentFilterLevel newValue;

    public ExplicitContentFilterLevelAuditLogChange() {
    }

    public ExplicitContentFilterLevelAuditLogChange(final String key, final ExplicitContentFilterLevel oldValue, final ExplicitContentFilterLevel newValue) {
        super(key);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public ExplicitContentFilterLevel getOldValue() {
        return oldValue;
    }

    public void setOldValue(final ExplicitContentFilterLevel oldValue) {
        this.oldValue = oldValue;
    }

    public ExplicitContentFilterLevel getNewValue() {
        return newValue;
    }

    public void setNewValue(final ExplicitContentFilterLevel newValue) {
        this.newValue = newValue;
    }
}
