package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Created by cobi on 12/30/2017.
 */
@Table(name = "channels_by_channel_group")
public class ChannelsByChannelGroup {
    @PartitionKey
    private Platform platform;
    @PartitionKey(1)
    @Column(name = "channel_group_id")
    private String channelGroupId;
    @ClusteringColumn
    private String id;
    @Column(name = "display_name")
    private String displayName;

    public ChannelsByChannelGroup() {
    }

    public ChannelsByChannelGroup(final Platform platform, final String id, final String displayName, final String channelGroupId) {
        this.platform = platform;
        this.id = id;
        this.displayName = displayName;
        this.channelGroupId = channelGroupId;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getChannelGroupId() {
        return channelGroupId;
    }

    public Platform getPlatform() {
        return platform;
    }

    public Channel toChannel() {
        return new Channel(platform, id, displayName, channelGroupId);
    }
}
