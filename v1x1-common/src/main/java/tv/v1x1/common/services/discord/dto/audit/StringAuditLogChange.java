package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 9/13/2017.
 */
public class StringAuditLogChange extends StringOrIntegerAuditLogChange {
    @JsonProperty("old_value")
    private String oldValue;
    @JsonProperty("new_value")
    private String newValue;

    public StringAuditLogChange() {
    }

    public StringAuditLogChange(final String key, final String oldValue, final String newValue) {
        super(key);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(final String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(final String newValue) {
        this.newValue = newValue;
    }
}
