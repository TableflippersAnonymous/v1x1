package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;

import java.util.UUID;

/**
 * Created by cobi on 10/15/2016.
 */
public abstract class Channel {

    @PartitionKey
    private String name;

    @Column(name = "tenant_id")
    private UUID tenantId;

    public Channel(String name, UUID tenantId) {
        this.name = name;
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public abstract tv.twitchbot.common.dto.core.Channel toCore(TenantAccessor accessor);
}
