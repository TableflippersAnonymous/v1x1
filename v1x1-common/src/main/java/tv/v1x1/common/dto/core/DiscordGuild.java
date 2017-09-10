package tv.v1x1.common.dto.core;

import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;
import tv.v1x1.common.dto.proto.core.PlatformOuterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cobi on 9/18/2017.
 */
public class DiscordGuild extends ChannelGroup {
    public static DiscordGuild fromProto(final ChannelOuterClass.ChannelGroup channelGroup) {
        final String id = channelGroup.getId();
        final Tenant tenant = Tenant.fromProto(channelGroup.getTenant());
        final String displayName = channelGroup.getDisplayName();
        final List<Channel> channels = new ArrayList<>();
        final DiscordGuild discordGuild = new DiscordGuild(id, tenant, displayName, channels);
        channels.addAll(channelGroup.getEntriesList().stream().map(c -> Channel.fromProto(discordGuild, c)).collect(Collectors.toList()));
        return discordGuild;
    }

    public static DiscordGuild fromProto(final Tenant tenant, final ChannelOuterClass.TenantEntry tenantEntry) {
        final String id = tenantEntry.getId();
        final String displayName = tenantEntry.getDisplayName();
        final List<Channel> channels = new ArrayList<>();
        final DiscordGuild discordGuild = new DiscordGuild(id, tenant, displayName, channels);
        channels.addAll(tenantEntry.getEntriesList().stream().map(c -> Channel.fromProto(discordGuild, c)).collect(Collectors.toList()));
        return discordGuild;
    }

    public DiscordGuild(final String id, final Tenant tenant, final String displayName, final List<Channel> channels) {
        super(id, tenant, displayName, channels);
    }

    @Override
    public Platform getPlatform() {
        return Platform.DISCORD;
    }
}
