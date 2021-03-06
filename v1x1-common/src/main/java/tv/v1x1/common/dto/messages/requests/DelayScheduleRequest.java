package tv.v1x1.common.dto.messages.requests;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by cobi on 10/22/2016.
 */
public class DelayScheduleRequest extends ScheduleRequest {
    private final long delay;

    public static DelayScheduleRequest fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final String responseQueueName, final UUID scheduleId, final byte[] payload, final RequestOuterClass.ScheduleRequest proto) {
        final long delay = proto.getDelay();
        return new DelayScheduleRequest(module, uuid, timestamp, context, responseQueueName, scheduleId, payload, delay);
    }

    public DelayScheduleRequest(final Module from, final String responseQueueName, final UUID scheduleId, final byte[] payload, final long delay) {
        super(from, responseQueueName, scheduleId, payload);
        this.delay = delay;
    }

    public DelayScheduleRequest(final Module from, final UUID messageId, final long timestamp, final Context context, final String responseQueueName, final UUID scheduleId, final byte[] payload, final long delay) {
        super(from, messageId, timestamp, context, responseQueueName, scheduleId, payload);
        this.delay = delay;
    }

    public long getDelay() {
        return delay;
    }

    @Override
    protected RequestOuterClass.ScheduleRequest.Builder toProtoSchedule() {
        return super.toProtoSchedule()
                .setType(RequestOuterClass.ScheduleRequest.Type.DELAY)
                .setDelay(delay);
    }
}
