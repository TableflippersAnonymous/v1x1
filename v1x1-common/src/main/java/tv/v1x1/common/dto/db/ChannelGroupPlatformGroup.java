package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "channel_group_platform_group")
public class ChannelGroupPlatformGroup {
    @PartitionKey
    private Platform platform;
    @PartitionKey(1)
    @Column(name = "channel_group_id")
    private String channelGroupId;
    @ClusteringColumn
    private String name;
    @Column(name = "display_name")
    private String displayName;

    public ChannelGroupPlatformGroup() {
    }

    public ChannelGroupPlatformGroup(final Platform platform, final String channelGroupId, final String name, final String displayName) {
        this.platform = platform;
        this.channelGroupId = channelGroupId;
        this.name = name;
        this.displayName = displayName;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(final Platform platform) {
        this.platform = platform;
    }

    public String getChannelGroupId() {
        return channelGroupId;
    }

    public void setChannelGroupId(final String channelGroupId) {
        this.channelGroupId = channelGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
}
