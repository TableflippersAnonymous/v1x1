package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by cobi on 10/16/2016.
 */
@Table(name = "inverse_tenant")
public class InverseTenant {
    @PartitionKey(0)
    private Platform platform;
    @PartitionKey(1)
    @Column(name = "channel_id")
    private String channelId;
    @Column(name = "tenant_id")
    private UUID tenantId;

    public InverseTenant(Platform platform, String channelId, UUID tenantId) {
        this.platform = platform;
        this.channelId = channelId;
        this.tenantId = tenantId;
    }

    public Platform getPlatform() {
        return platform;
    }

    public String getChannelId() {
        return channelId;
    }

    public UUID getTenantId() {
        return tenantId;
    }
}
