package tv.v1x1.common.dto.core;

import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/26/2017.
 */
public class TwitchChannelGroup extends ChannelGroup {
    public static TwitchChannelGroup fromProto(final ChannelOuterClass.ChannelGroup channelGroup) {
        final String id = channelGroup.getId();
        final tv.v1x1.common.dto.core.Tenant tenant = tv.v1x1.common.dto.core.Tenant.fromProto(channelGroup.getTenant());
        final String displayName = channelGroup.getDisplayName();
        final List<Channel> channels = new ArrayList<>();
        final TwitchChannelGroup twitchChannelGroup = new TwitchChannelGroup(id, tenant, displayName, channels);
        channels.addAll(channelGroup.getEntriesList().stream().map(c -> Channel.fromProto(twitchChannelGroup, c)).collect(Collectors.toList()));
        return twitchChannelGroup;
    }

    public static TwitchChannelGroup fromProto(final tv.v1x1.common.dto.core.Tenant tenant, final ChannelOuterClass.TenantEntry tenantEntry) {
        final String id = tenantEntry.getId();
        final String displayName = tenantEntry.getDisplayName();
        final List<Channel> channels = new ArrayList<>();
        final TwitchChannelGroup twitchChannelGroup = new TwitchChannelGroup(id, tenant, displayName, channels);
        channels.addAll(tenantEntry.getEntriesList().stream().map(c -> Channel.fromProto(twitchChannelGroup, c)).collect(Collectors.toList()));
        return twitchChannelGroup;
    }

    public TwitchChannelGroup(final String id, final tv.v1x1.common.dto.core.Tenant tenant, final String displayName, final List<Channel> channels) {
        super(id, tenant, displayName, channels);
    }

    @Override
    public Platform getPlatform() {
        return Platform.TWITCH;
    }
}
