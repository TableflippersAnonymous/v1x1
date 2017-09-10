package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.ChannelGroup;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Created by cobi on 4/8/2018.
 */
public class ChannelGroupConfigChangeEvent extends ConfigChangeEvent {
    private final ChannelGroup channelGroup;

    public ChannelGroupConfigChangeEvent(final Module from, final Module configModule, final ChannelGroup channelGroup) {
        super(from, configModule);
        this.channelGroup = channelGroup;
    }

    public ChannelGroupConfigChangeEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final Module configModule, final ChannelGroup channelGroup) {
        super(from, messageId, timestamp, context, configModule);
        this.channelGroup = channelGroup;
    }

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    @Override
    protected EventOuterClass.ConfigChangeEvent.Builder toProtoConfigChange() {
        return super.toProtoConfigChange()
                .setConfigType(EventOuterClass.ConfigChangeEvent.ConfigType.CHANNEL_GROUP)
                .setChannelGroup(channelGroup.toProto());
    }
}
