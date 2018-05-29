package tv.v1x1.common.services.discord.resources;

import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.audit.AuditLog;
import tv.v1x1.common.services.discord.dto.audit.AuditLogEventType;

import javax.ws.rs.client.WebTarget;

/**
 * Created by cobi on 9/11/2017.
 */
public class AuditLogsResource {
    private final WebTarget guilds;

    public AuditLogsResource(final WebTarget guilds) {
        this.guilds = guilds;
    }

    public AuditLog getGuildAuditLog(final String guildId, final String userId,
                                     final AuditLogEventType auditLogEventType, final String before,
                                     final Integer limit) {
        return guilds.path(guildId).path("audit-logs")
                .queryParam("user_id", userId).queryParam("action_type", auditLogEventType.getValue())
                .queryParam("before", before).queryParam("limit", limit)
                .request(DiscordApi.ACCEPT)
                .get(AuditLog.class);
    }
}
