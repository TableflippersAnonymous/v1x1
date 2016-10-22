package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.*;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.DiscordChannel;
import tv.v1x1.common.dto.core.TwitchChannel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/15/2016.
 */
@Table(name = "tenant")
public class Tenant {
    @UDT(name = "tenant_entry")
    public static class Entry {
        private Platform platform;
        @Field(name = "display_name")
        private String displayName;
        @Field(name = "channel_id")
        private String channelId;

        public Entry() {
        }

        public Entry(final Platform platform, final String displayName, final String channelId) {
            this.platform = platform;
            this.displayName = displayName;
            this.channelId = channelId;
        }

        public Platform getPlatform() {
            return platform;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getChannelId() {
            return channelId;
        }

        public tv.v1x1.common.dto.core.Channel toCore(final tv.v1x1.common.dto.core.Tenant tenant) {
            switch(platform) {
                case DISCORD: return new DiscordChannel(channelId, tenant, displayName);
                case TWITCH: return new TwitchChannel(channelId, tenant, displayName);
                default: throw new IllegalStateException("Unknown channel platform " + platform.name());
            }
        }
    }

    @PartitionKey
    private UUID id;
    @Column(caseSensitive = true, name = "entries")
    private List<Entry> entries;

    public Tenant() {
    }

    public Tenant(final UUID id, final List<Entry> entries) {
        this.id = id;
        this.entries = entries;
    }

    public UUID getId() {
        return id;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public tv.v1x1.common.dto.core.Tenant toCore() {
        final List<Channel> channels = new ArrayList<>();
        final tv.v1x1.common.dto.core.Tenant tenant = new tv.v1x1.common.dto.core.Tenant(
                new tv.v1x1.common.dto.core.UUID(id),
                channels
        );
        channels.addAll(entries.stream().map(entry -> entry.toCore(tenant)).collect(Collectors.toList()));
        return tenant;
    }
}
