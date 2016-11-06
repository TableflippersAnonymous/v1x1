package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by naomi on 11/5/2016.
 */
@Table(name = "channel_configuration")
public class ChannelConfiguration {
    @PartitionKey(0)
    private String module;
    @PartitionKey(1)
    @Column(name = "tenant_id")
    private UUID tenantId;
    @ClusteringColumn(0)
    private Platform platform;
    @Column(name = "channel_id")
    @ClusteringColumn(1)
    private String channelId;
    private String json;

    public ChannelConfiguration() {
    }

    public ChannelConfiguration(final String module, final UUID tenantId, final Platform platform, final String channelId, final String json) {
        this.module = module;
        this.tenantId = tenantId;
        this.platform = platform;
        this.channelId = channelId;
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

    public Platform getPlatform() {
        return platform;
    }

    public String getChannelId() {
        return channelId;
    }
}