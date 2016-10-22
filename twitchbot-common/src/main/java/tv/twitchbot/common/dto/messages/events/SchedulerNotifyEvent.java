package tv.twitchbot.common.dto.messages.events;

import com.google.protobuf.ByteString;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Created by naomi on 10/22/2016.
 */
public class SchedulerNotifyEvent extends Event {
    private final Module module;
    private final UUID id;
    private final byte[] payload;

    public static SchedulerNotifyEvent fromProto(final Module module, final UUID uuid, final long timestamp, final EventOuterClass.SchedulerNotifyEvent proto) {
        final Module requestingModule = Module.fromProto(proto.getModule());
        final UUID timerId = UUID.fromProto(proto.getId());
        final byte[] payload = proto.getPayload().toByteArray();
        return new SchedulerNotifyEvent(module, uuid, timestamp, requestingModule, timerId, payload);
    }

    public SchedulerNotifyEvent(final Module from, final Module module, final UUID id, final byte[] payload) {
        super(from);
        this.module = module;
        this.id = id;
        this.payload = payload;
    }

    public SchedulerNotifyEvent(final Module from, final UUID messageId, final long timestamp, final Module module, final UUID id, final byte[] payload) {
        super(from, messageId, timestamp);
        this.module = module;
        this.id = id;
        this.payload = payload;
    }

    public Module getModule() {
        return module;
    }

    public UUID getId() {
        return id;
    }

    public byte[] getPayload() {
        return payload;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.SCHEDULER_NOTIFY)
                .setExtension(EventOuterClass.SchedulerNotifyEvent.data, toProtoSchedulerNotify().build());
    }

    private EventOuterClass.SchedulerNotifyEvent.Builder toProtoSchedulerNotify() {
        return EventOuterClass.SchedulerNotifyEvent.newBuilder()
                .setModule(module.toProto())
                .setId(id.toProto())
                .setPayload(ByteString.copyFrom(payload));
    }
}
