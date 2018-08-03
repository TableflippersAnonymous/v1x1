package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dto.core.ChannelGroup;
import tv.v1x1.common.dto.core.DiscordChannel;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.TwitchChannel;

/**
 * Created by naomi on 10/15/2016.
 */
@Table(name = "channel")
public class Channel {
    @PartitionKey
    private Platform platform;
    @PartitionKey(1)
    private String id;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "channel_group_id")
    private String channelGroupId;

    public Channel() {
    }

    public Channel(final Platform platform, final String id, final String displayName, final String channelGroupId) {
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

    public tv.v1x1.common.dto.core.Channel toCore(final ChannelGroup channelGroup) {
        switch(platform) {
            case TWITCH: return new TwitchChannel(id, channelGroup, displayName);
            case DISCORD: return new DiscordChannel(id, channelGroup, displayName);
            default: throw new IllegalStateException("Unknown platform " + platform);
        }
    }

    public tv.v1x1.common.dto.core.Channel toCore(final DAOTenant daoTenant) {
        final Tenant tenant = daoTenant.getByChannel(this).toCore(daoTenant);
        return tenant.getChannel(platform, channelGroupId, id).orElse(null);
    }
}
