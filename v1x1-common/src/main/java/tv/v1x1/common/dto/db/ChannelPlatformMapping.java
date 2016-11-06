package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by naomi on 11/5/2016.
 */
@Table(name = "channel_platform_mapping")
public class ChannelPlatformMapping {
    @PartitionKey(0)
    private Platform platform;
    @PartitionKey(1)
    @Column(name = "channel_id")
    private String channelId;
    @ClusteringColumn(0)
    @Column(name = "platform_group")
    private String platformGroup;
    @Column(name = "group_id")
    private UUID groupId;

    public ChannelPlatformMapping() {
    }

    public ChannelPlatformMapping(final Platform platform, final String channelId, final String platformGroup, final UUID groupId) {
        this.platform = platform;
        this.channelId = channelId;
        this.platformGroup = platformGroup;
        this.groupId = groupId;
    }

    public Platform getPlatform() {
        return platform;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getPlatformGroup() {
        return platformGroup;
    }

    public UUID getGroupId() {
        return groupId;
    }
}
