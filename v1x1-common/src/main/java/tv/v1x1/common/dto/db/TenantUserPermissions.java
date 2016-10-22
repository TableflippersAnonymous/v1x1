package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.UDT;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by naomi on 10/15/2016.
 */
@Table(name = "tenant_user_permissions")
public class TenantUserPermissions {
    @UDT(name = "permission")
    public static class Permission {
        private String node;

        public Permission() {
        }

        public Permission(final String node) {
            this.node = node;
        }

        public String getNode() {
            return node;
        }

        public tv.v1x1.common.dto.core.Permission toCore() {
            return new tv.v1x1.common.dto.core.Permission(node);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final Permission that = (Permission) o;

            return node != null ? node.equals(that.node) : that.node == null;

        }

        @Override
        public int hashCode() {
            return node != null ? node.hashCode() : 0;
        }
    }

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

    public Collection<Permission> getPermissions() {
        return permissions;
    }
}
