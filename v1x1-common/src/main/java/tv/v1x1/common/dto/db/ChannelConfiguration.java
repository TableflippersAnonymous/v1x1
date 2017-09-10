package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by cobi on 11/5/2016.
 */
@Table(name = "channel_configuration_v2")
public class ChannelConfiguration {
    @PartitionKey(0)
    private String module;
    @PartitionKey(1)
    private Platform platform;
    @PartitionKey(2)
    @Column(name = "channel_group_id")
    private String channelGroupId;
    @ClusteringColumn(0)
    @Column(name = "channel_id")
    private String channelId;
    private boolean enabled;
    private String json;

    public ChannelConfiguration() {
    }

    public ChannelConfiguration(final String module, final Platform platform, final String channelGroupId, final String channelId, final boolean enabled, final String json) {
        this.module = module;
        this.platform = platform;
        this.channelGroupId = channelGroupId;
        this.channelId = channelId;
        this.enabled = enabled;
        this.json = json;
    }

    public String getModule() {
        return module;
    }

    public String getChannelGroupId() {
        return channelGroupId;
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

    public boolean isEnabled() {
        return enabled;
    }
}