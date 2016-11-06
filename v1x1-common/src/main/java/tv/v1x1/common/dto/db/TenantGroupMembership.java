package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by cobi on 11/5/2016.
 */
@Table(name = "tenant_group_membership")
public class TenantGroupMembership {
    @PartitionKey(0)
    @Column(name = "tenant_id")
    private UUID tenantId;
    @PartitionKey(1)
    @Column(name = "group_id")
    private UUID groupId;
    @ClusteringColumn(0)
    @Column(name = "user_id")
    private UUID userId;

    public TenantGroupMembership() {
    }

    public TenantGroupMembership(final UUID tenantId, final UUID groupId, final UUID userId) {
        this.tenantId = tenantId;
        this.groupId = groupId;
        this.userId = userId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public UUID getUserId() {
        return userId;
    }
}
