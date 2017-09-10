package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/16/2017.
 */
public class IntegerAuditLogChange extends StringOrIntegerAuditLogChange {
    @JsonProperty("old_value")
    private Integer oldValue;
    @JsonProperty("new_value")
    private Integer newValue;

    public IntegerAuditLogChange() {
    }

    public IntegerAuditLogChange(final String key, final Integer oldValue, final Integer newValue) {
        super(key);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Integer getOldValue() {
        return oldValue;
    }

    public void setOldValue(final Integer oldValue) {
        this.oldValue = oldValue;
    }

    public Integer getNewValue() {
        return newValue;
    }

    public void setNewValue(final Integer newValue) {
        this.newValue = newValue;
    }
}
