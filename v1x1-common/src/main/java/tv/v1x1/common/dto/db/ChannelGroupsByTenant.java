package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by naomi on 12/30/2017.
 */
@Table(name = "channel_groups_by_tenant")
public class ChannelGroupsByTenant {
    @PartitionKey
    @Column(name = "tenant_id")
    private UUID tenantId;
    @ClusteringColumn
    private Platform platform;
    @ClusteringColumn(1)
    private String id;
    @Column(name = "display_name")
    private String displayName;

    public ChannelGroupsByTenant() {
    }

    public ChannelGroupsByTenant(final Platform platform, final String id, final String displayName, final UUID tenantId) {
        this.platform = platform;
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

    public Platform getPlatform() {
        return platform;
    }

    public ChannelGroup toChannelGroup() {
        return new ChannelGroup(platform, id, displayName, tenantId);
    }
}
