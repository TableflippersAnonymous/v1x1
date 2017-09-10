package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChannelGroup;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Created by naomi on 2/17/2017.
 */
public class ConfigChangeEvent extends Event {
    public static ConfigChangeEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.ConfigChangeEvent proto) {
        final Module configModule = Module.fromProto(proto.getModule());
        final Tenant tenant = proto.hasTenant() ? Tenant.fromProto(proto.getTenant()) : null;
        final ChannelGroup channelGroup = proto.hasChannelGroup() ? ChannelGroup.fromProto(proto.getChannelGroup()) : null;
        final Channel channel = proto.hasChannel() ? Channel.fromProto(proto.getChannel()) : null;
        switch(proto.getConfigType()) {
            case GLOBAL: return new GlobalConfigChangeEvent(module, uuid, timestamp, context, configModule);
            case TENANT: return new TenantConfigChangeEvent(module, uuid, timestamp, context, configModule, tenant);
            case CHANNEL_GROUP: return new ChannelGroupConfigChangeEvent(module, uuid, timestamp, context, configModule, channelGroup);
            case CHANNEL: return new ChannelConfigChangeEvent(module, uuid, timestamp, context, configModule, channel);
            default: throw new IllegalStateException("Unknown Config type: " + proto.getConfigType());
        }
    }

    private final Module configModule;

    public ConfigChangeEvent(final Module from, final Module configModule) {
        super(from);
        this.configModule = configModule;
    }

    public ConfigChangeEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final Module configModule) {
        super(from, messageId, timestamp, context);
        this.configModule = configModule;
    }

    public Module getConfigModule() {
        return configModule;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.CONFIG_CHANGE)
                .setExtension(EventOuterClass.ConfigChangeEvent.data, toProtoConfigChange().build());
    }

    protected EventOuterClass.ConfigChangeEvent.Builder toProtoConfigChange() {
        return EventOuterClass.ConfigChangeEvent.newBuilder()
                .setModule(configModule.toProto());
    }
}
