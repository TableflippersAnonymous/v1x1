package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by cobi on 11/5/2016.
 */
@Table(name = "channel_group_platform_mapping")
public class ChannelGroupPlatformMapping {
    @PartitionKey(0)
    private Platform platform;
    @PartitionKey(1)
    @Column(name = "channel_group_id")
    private String channelGroupId;
    @ClusteringColumn(0)
    @Column(name = "platform_group")
    private String platformGroup;
    @Column(name = "group_id")
    private UUID groupId;

    public ChannelGroupPlatformMapping() {
    }

    public ChannelGroupPlatformMapping(final Platform platform, final String channelGroupId, final String platformGroup, final UUID groupId) {
        this.platform = platform;
        this.channelGroupId = channelGroupId;
        this.platformGroup = platformGroup;
        this.groupId = groupId;
    }

    public Platform getPlatform() {
        return platform;
    }

    public String getChannelGroupId() {
        return channelGroupId;
    }

    public String getPlatformGroup() {
        return platformGroup;
    }

    public UUID getGroupId() {
        return groupId;
    }
}
