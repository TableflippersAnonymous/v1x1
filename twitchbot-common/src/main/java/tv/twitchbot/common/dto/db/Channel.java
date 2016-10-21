package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;

import java.util.UUID;

/**
 * Created by naomi on 10/15/2016.
 */
public abstract class Channel {

    @PartitionKey
    private String id;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "tenant_id")
    private UUID tenantId;

    public Channel() {
    }

    public Channel(final String id, final String displayName, final UUID tenantId) {
        this.id = id;
        this.displayName = displayName;
        this.tenantId = tenantId;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public abstract tv.twitchbot.common.dto.core.Channel toCore(TenantAccessor accessor);
}
