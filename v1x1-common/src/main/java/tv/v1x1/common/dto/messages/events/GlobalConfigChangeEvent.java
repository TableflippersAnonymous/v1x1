package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Created by cobi on 2/17/2017.
 */
public class GlobalConfigChangeEvent extends ConfigChangeEvent {
    public GlobalConfigChangeEvent(final Module from, final Module configModule) {
        super(from, configModule);
    }

    public GlobalConfigChangeEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final Module configModule) {
        super(from, messageId, timestamp, context, configModule);
    }

    @Override
    protected EventOuterClass.ConfigChangeEvent.Builder toProtoConfigChange() {
        return super.toProtoConfigChange()
                .setConfigType(EventOuterClass.ConfigChangeEvent.ConfigType.GLOBAL);
    }
}
