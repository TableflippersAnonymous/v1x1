package tv.twitchbot.common.dto.messages.requests;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cobi on 10/22/2016.
 */
public class CronScheduleRequest extends ScheduleRequest {
    private final Set<Integer> minute;
    private final Set<Integer> hour;
    private final Set<Integer> dayOfMonth;
    private final Set<Integer> month;
    private final Set<Integer> dayOfWeek;

    public static CronScheduleRequest fromProto(final Module module, final UUID uuid, final long timestamp, final String responseQueueName, final UUID scheduleId,
                                            final byte[] payload, final RequestOuterClass.ScheduleRequest proto) {
        final Set<Integer> minute = new HashSet<>(proto.getCron().getMinuteList());
        final Set<Integer> hour = new HashSet<>(proto.getCron().getHourList());
        final Set<Integer> dayOfMonth = new HashSet<>(proto.getCron().getDayOfMonthList());
        final Set<Integer> month = new HashSet<>(proto.getCron().getMonthList());
        final Set<Integer> dayOfWeek = new HashSet<>(proto.getCron().getDayOfWeekList());
        return new CronScheduleRequest(module, uuid, timestamp, responseQueueName, scheduleId, payload, minute, hour, dayOfMonth, month, dayOfWeek);
    }


    public CronScheduleRequest(final Module from, final String responseQueueName, final UUID scheduleId, final byte[] payload,
                               final Set<Integer> minute, final Set<Integer> hour, final Set<Integer> dayOfMonth, final Set<Integer> month, final Set<Integer> dayOfWeek) {
        super(from, responseQueueName, scheduleId, payload);
        this.minute = minute;
        this.hour = hour;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.dayOfWeek = dayOfWeek;
    }

    public CronScheduleRequest(final Module from, final UUID messageId, final long timestamp, final String responseQueueName, final UUID scheduleId, final byte[] payload,
                               final Set<Integer> minute, final Set<Integer> hour, final Set<Integer> dayOfMonth, final Set<Integer> month, final Set<Integer> dayOfWeek) {
        super(from, messageId, timestamp, responseQueueName, scheduleId, payload);
        this.minute = minute;
        this.hour = hour;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.dayOfWeek = dayOfWeek;
    }

    public Set<Integer> getMinute() {
        return minute;
    }

    public Set<Integer> getHour() {
        return hour;
    }

    public Set<Integer> getDayOfMonth() {
        return dayOfMonth;
    }

    public Set<Integer> getMonth() {
        return month;
    }

    public Set<Integer> getDayOfWeek() {
        return dayOfWeek;
    }

    @Override
    protected RequestOuterClass.ScheduleRequest.Builder toProtoSchedule() {
        return super.toProtoSchedule()
                .setType(RequestOuterClass.ScheduleRequest.Type.CRON)
                .setCron(
                        RequestOuterClass.ScheduleRequest.Cron.newBuilder()
                            .addAllMinute(minute)
                            .addAllHour(hour)
                            .addAllDayOfMonth(dayOfMonth)
                            .addAllMonth(month)
                            .addAllDayOfWeek(dayOfWeek)
                            .build()
                );
    }
}
