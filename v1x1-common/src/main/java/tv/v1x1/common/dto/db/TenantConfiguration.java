package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by cobi on 10/17/2016.
 */
@Table(name = "tenant_configuration")
public class TenantConfiguration {
    @PartitionKey(0)
    private String module;
    @PartitionKey(1)
    @Column(name = "tenant_id")
    private UUID tenantId;
    private String json;

    public TenantConfiguration() {
    }

    public TenantConfiguration(final String module, final UUID tenantId, final String json) {
        this.module = module;
        this.tenantId = tenantId;
        this.json = json;
    }

    public String getModule() {
        return module;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getJson() {
        return json;
    }
}
