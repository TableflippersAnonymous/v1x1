package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/16/2017.
 */
public abstract class StringOrIntegerAuditLogChange extends AuditLogChange {
    @JsonCreator
    public static StringOrIntegerAuditLogChange create(@JsonProperty("key") final String key,
                                                       @JsonProperty("old_value") final Object oldValue,
                                                       @JsonProperty("new_value") final Object newValue) {
        if(oldValue instanceof Integer && newValue instanceof Integer)
            return new IntegerAuditLogChange(key, (Integer) oldValue, (Integer) newValue);
        if(oldValue instanceof String && newValue instanceof String)
            return new StringAuditLogChange(key, (String) oldValue, (String) newValue);
        throw new IllegalArgumentException("oldValue and newValue must be of the same type and must be either String or Integer");
    }

    public StringOrIntegerAuditLogChange() {
    }

    public StringOrIntegerAuditLogChange(final String key) {
        super(key);
    }
}
