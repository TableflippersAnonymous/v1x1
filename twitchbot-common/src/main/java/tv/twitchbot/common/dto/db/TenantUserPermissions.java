package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.UDT;
import tv.twitchbot.common.dto.core.Permission;

import java.util.List;
import java.util.UUID;

/**
 * Created by cobi on 10/15/2016.
 */
@Table(name = "tenant_user_permissions")
public class TenantUserPermissions {
    @UDT(name = "permission")
    public static class Permission {
        private String node;

        public Permission(String node) {
            this.node = node;
        }

        public String getNode() {
            return node;
        }

        public tv.twitchbot.common.dto.core.Permission toCore() {
            return new tv.twitchbot.common.dto.core.Permission(node);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Permission that = (Permission) o;

            return node != null ? node.equals(that.node) : that.node == null;

        }

        @Override
        public int hashCode() {
            return node != null ? node.hashCode() : 0;
        }
    }

    @PartitionKey
    private UUID tenantId;
    @ClusteringColumn
    private UUID userId;
    private List<Permission> permissions;

    public TenantUserPermissions(UUID tenantId, UUID userId, List<Permission> permissions) {
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
