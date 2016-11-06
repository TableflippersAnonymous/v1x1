package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.List;
import java.util.UUID;

/**
 * Created by cobi on 11/5/2016.
 */
@Table(name = "tenant_group")
public class TenantGroup {
    @PartitionKey
    @Column(name = "tenant_id")
    private UUID tenantId;
    @ClusteringColumn
    @Column(name = "group_id")
    private UUID groupId;
    private String name;
    private List<Permission> permissions;

    public TenantGroup() {
    }

    public TenantGroup(final UUID tenantId, final UUID groupId, final String name, final List<Permission> permissions) {
        this.tenantId = tenantId;
        this.groupId = groupId;
        this.name = name;
        this.permissions = permissions;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }
}
