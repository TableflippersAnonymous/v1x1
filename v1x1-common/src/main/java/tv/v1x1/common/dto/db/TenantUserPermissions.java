package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.List;
import java.util.UUID;

/**
 * @author Naomi
 */
@Table(name = "tenant_user_permissions")
public class TenantUserPermissions {

    @PartitionKey
    @Column(name = "tenant_id")
    private UUID tenantId;
    @ClusteringColumn
    @Column(name = "user_id")
    private UUID userId;
    private List<Permission> permissions;

    public TenantUserPermissions() {
    }

    public TenantUserPermissions(final UUID tenantId, final UUID userId, final List<Permission> permissions) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.permissions = permissions;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }
}
