package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dto.core.*;
import tv.v1x1.common.dto.core.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/26/2017.
 */
@Table(name = "channel_group")
public class ChannelGroup {
    @PartitionKey
    private Platform platform;
    @PartitionKey(1)
    private String id;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "tenant_id")
    private UUID tenantId;

    public ChannelGroup() {
    }

    public ChannelGroup(final Platform platform, final String id, final String displayName, final UUID tenantId) {
        this.platform = platform;
        this.id = id;
        this.displayName = displayName;
        this.tenantId = tenantId;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public Platform getPlatform() {
        return platform;
    }

    public tv.v1x1.common.dto.core.ChannelGroup toCore(final tv.v1x1.common.dto.core.Tenant tenant, final DAOTenant daoTenant) {
        final List<Channel> channels = new ArrayList<>();
        final tv.v1x1.common.dto.core.ChannelGroup channelGroup;
        switch(platform) {
            case TWITCH: channelGroup = new TwitchChannelGroup(id, tenant, displayName, channels); break;
            case DISCORD: channelGroup = new DiscordGuild(id, tenant, displayName, channels); break;
            default: throw new IllegalStateException("Unknown platform " + platform);
        }
        final Set<tv.v1x1.common.dto.db.Channel> entries = daoTenant.getChannelsByChannelGroup(this);
        channels.addAll(entries.stream().map(entry -> entry.toCore(channelGroup)).collect(Collectors.toList()));
        return channelGroup;
    }
}
