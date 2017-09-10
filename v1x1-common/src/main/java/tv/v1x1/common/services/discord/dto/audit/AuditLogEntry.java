package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 9/11/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditLogEntry {
    @JsonProperty("target_id")
    private String targetId;
    @JsonProperty
    private List<AuditLogChange> changes;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty
    private String id;
    @JsonProperty("action_type")
    private AuditLogEventType actionType;
    @JsonProperty
    private List<AuditEntryInfo> options;
    @JsonProperty
    private String reason;

    public AuditLogEntry() {
    }

    public AuditLogEntry(final String targetId, final List<AuditLogChange> changes, final String userId,
                         final String id, final AuditLogEventType actionType, final List<AuditEntryInfo> options,
                         final String reason) {
        this.targetId = targetId;
        this.changes = changes;
        this.userId = userId;
        this.id = id;
        this.actionType = actionType;
        this.options = options;
        this.reason = reason;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(final String targetId) {
        this.targetId = targetId;
    }

    public List<AuditLogChange> getChanges() {
        return changes;
    }

    public void setChanges(final List<AuditLogChange> changes) {
        this.changes = changes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public AuditLogEventType getActionType() {
        return actionType;
    }

    public void setActionType(final AuditLogEventType actionType) {
        this.actionType = actionType;
    }

    public List<AuditEntryInfo> getOptions() {
        return options;
    }

    public void setOptions(final List<AuditEntryInfo> options) {
        this.options = options;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }
}
