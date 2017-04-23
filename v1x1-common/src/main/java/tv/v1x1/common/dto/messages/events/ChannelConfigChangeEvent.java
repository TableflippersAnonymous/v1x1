package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Created by naomi on 2/17/2017.
 */
public class ChannelConfigChangeEvent extends ConfigChangeEvent {
    private final Channel channel;

    public ChannelConfigChangeEvent(final Module from, final Module configModule, final Channel channel) {
        super(from, configModule);
        this.channel = channel;
    }

    public ChannelConfigChangeEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final Module configModule, final Channel channel) {
        super(from, messageId, timestamp, context, configModule);
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    protected EventOuterClass.ConfigChangeEvent.Builder toProtoConfigChange() {
        return super.toProtoConfigChange()
                .setConfigType(EventOuterClass.ConfigChangeEvent.ConfigType.CHANNEL)
                .setChannel(channel.toProto());
    }
}
