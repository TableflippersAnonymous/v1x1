package tv.v1x1.common.dto.messages.requests;

import com.google.protobuf.ByteString;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Request;
import tv.v1x1.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by naomi on 10/22/2016.
 */
public abstract class ScheduleRequest extends Request {
    private final UUID scheduleId;
    private final byte[] payload;

    public static ScheduleRequest fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final String responseQueueName, final RequestOuterClass.ScheduleRequest proto) {
        final UUID scheduleId = UUID.fromProto(proto.getId());
        final byte[] payload = proto.getPayload().toByteArray();
        switch(proto.getType()) {
            case DELAY: return DelayScheduleRequest.fromProto(module, uuid, timestamp, context, responseQueueName, scheduleId, payload, proto);
            case INTERVAL: return IntervalScheduleRequest.fromProto(module, uuid, timestamp, context, responseQueueName, scheduleId, payload, proto);
            case CRON: return CronScheduleRequest.fromProto(module, uuid, timestamp, context, responseQueueName, scheduleId, payload, proto);
            default: throw new IllegalStateException("Unknown Schedule type: " + proto.getType().name());
        }
    }

    public ScheduleRequest(final Module from, final String responseQueueName, final UUID scheduleId, final byte[] payload) {
        super(from, responseQueueName);
        this.scheduleId = scheduleId;
        this.payload = payload;
    }

    public ScheduleRequest(final Module from, final UUID messageId, final long timestamp, final Context context, final String responseQueueName, final UUID scheduleId, final byte[] payload) {
        super(from, messageId, timestamp, context, responseQueueName);
        this.scheduleId = scheduleId;
        this.payload = payload;
    }

    public UUID getScheduleId() {
        return scheduleId;
    }

    public byte[] getPayload() {
        return payload;
    }

    @Override
    protected RequestOuterClass.Request.Builder toProtoRequest() {
        return super.toProtoRequest()
                .setType(RequestOuterClass.RequestType.SCHEDULE)
                .setExtension(RequestOuterClass.ScheduleRequest.data, toProtoSchedule().build());
    }

    protected RequestOuterClass.ScheduleRequest.Builder toProtoSchedule() {
        return RequestOuterClass.ScheduleRequest.newBuilder()
                .setId(scheduleId.toProto())
                .setPayload(ByteString.copyFrom(payload));
    }
}
