package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.user.User;
import tv.v1x1.common.services.discord.dto.webhook.Webhook;

import java.util.List;

/**
 * Created by cobi on 9/11/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditLog {
    @JsonProperty
    private List<Webhook> webhooks;
    @JsonProperty
    private List<User> users;
    @JsonProperty("audit_log_entries")
    private List<AuditLogEntry> auditLogEntries;

    public AuditLog() {
    }

    public AuditLog(final List<Webhook> webhooks, final List<User> users, final List<AuditLogEntry> auditLogEntries) {
        this.webhooks = webhooks;
        this.users = users;
        this.auditLogEntries = auditLogEntries;
    }

    public List<Webhook> getWebhooks() {
        return webhooks;
    }

    public void setWebhooks(final List<Webhook> webhooks) {
        this.webhooks = webhooks;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(final List<User> users) {
        this.users = users;
    }

    public List<AuditLogEntry> getAuditLogEntries() {
        return auditLogEntries;
    }

    public void setAuditLogEntries(final List<AuditLogEntry> auditLogEntries) {
        this.auditLogEntries = auditLogEntries;
    }
}
