package tv.v1x1.modules.channel.factoids.dao;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.db.Permission;

import java.util.UUID;

/**
 * @author Josh
 */
@Table(name = "factoid")
public class Factoid {
    @PartitionKey
    @Column(name = "tenant_id")
    private UUID tenantId;
    @ClusteringColumn
    private String id;
    private Permission permission;
    private boolean alias;
    private String data;

    private Factoid() {
        // Intentionally empty
    }

    public Factoid(final String id, final Tenant tenant, final String data, final Permission permission, final boolean alias) {
        this.tenantId = tenant.getId().getValue();
        this.id = id.toLowerCase();
        this.data = data;
        this.alias = alias;
        if(permission == null)
            this.permission = null;
        else
            this.permission = new tv.v1x1.common.dto.db.Permission(permission.getNode());
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(final Permission permission) {
        this.permission = permission;
    }

    public boolean isAlias() {
        return alias;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }
}
