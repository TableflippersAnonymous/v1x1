package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by cobi on 11/5/2016.
 */
@Table(name = "channel_group_configuration")
public class ChannelGroupConfiguration implements Configuration {
    @PartitionKey(0)
    private String module;
    @PartitionKey(1)
    @Column(name = "tenant_id")
    private UUID tenantId;
    @ClusteringColumn(0)
    private Platform platform;
    @Column(name = "channel_group_id")
    @ClusteringColumn(1)
    private String channelGroupId;
    private boolean enabled;
    private String json;

    public ChannelGroupConfiguration() {
    }

    public ChannelGroupConfiguration(final String module, final UUID tenantId, final Platform platform, final String channelGroupId, final boolean enabled, final String json) {
        this.module = module;
        this.tenantId = tenantId;
        this.platform = platform;
        this.channelGroupId = channelGroupId;
        this.enabled = enabled;
        this.json = json;
    }

    public String getModule() {
        return module;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public Platform getPlatform() {
        return platform;
    }

    public String getChannelGroupId() {
        return channelGroupId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getJson() {
        return json;
    }
}