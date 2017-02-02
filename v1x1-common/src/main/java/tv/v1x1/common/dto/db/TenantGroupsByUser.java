package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by naomi on 11/5/2016.
 */
@Table(name = "tenant_groups_by_user")
public class TenantGroupsByUser {
    @PartitionKey
    @Column(name = "user_id")
    private UUID userId;
    @ClusteringColumn
    @Column(name = "tenant_id")
    private UUID tenantId;
    private List<UUID> groups = new ArrayList<>();

    public TenantGroupsByUser() {
    }

    public TenantGroupsByUser(final UUID userId, final UUID tenantId, final List<UUID> groups) {
        this.userId = userId;
        this.tenantId = tenantId;
        this.groups = groups;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public List<UUID> getGroups() {
        return groups == null ? new ArrayList<>() : groups;
    }
}
