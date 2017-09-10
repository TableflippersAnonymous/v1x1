package tv.v1x1.common.rpc.client;

import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.requests.CronScheduleRequest;
import tv.v1x1.common.dto.messages.requests.DelayScheduleRequest;
import tv.v1x1.common.dto.messages.requests.IntervalScheduleRequest;
import tv.v1x1.common.dto.messages.requests.ScheduleRequest;
import tv.v1x1.common.dto.messages.responses.ScheduleResponse;
import tv.v1x1.common.modules.Module;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Created by naomi on 10/23/2016.
 */
public class SchedulerServiceClient extends ServiceClient<ScheduleRequest, ScheduleResponse> {
    public SchedulerServiceClient(final Module<?, ?> module) {
        super(module, ScheduleResponse.class);
    }

    @Override
    protected String getServiceName() {
        return "Scheduler";
    }

    public Future<ScheduleResponse> schedule(final Set<Integer> minute, final Set<Integer> hour, final Set<Integer> dayOfMonth,
                                             final Set<Integer> month, final Set<Integer> dayOfWeek, final UUID id, final byte[] payload) {
        final CronScheduleRequest cronScheduleRequest = new CronScheduleRequest(getModule(), getQueueName(), id, payload, minute, hour, dayOfMonth, month, dayOfWeek);
        return send(cronScheduleRequest);
    }

    public Future<ScheduleResponse> scheduleAt(final long timestamp, final UUID id, final byte[] payload) {
        final DelayScheduleRequest delayScheduleRequest = new DelayScheduleRequest(getModule(), getQueueName(), id, payload, new Date().getTime() - timestamp);
        return send(delayScheduleRequest);
    }

    public Future<ScheduleResponse> scheduleWithDelay(final long delay, final UUID id, final byte[] payload) {
        final DelayScheduleRequest delayScheduleRequest = new DelayScheduleRequest(getModule(), getQueueName(), id, payload, delay);
        return send(delayScheduleRequest);
    }

    /**
     * Schedule a task to be run and repeated in the future
     * @param interval time in milliseconds; or -1 to cancel an existing one
     * @param id the ID of the task
     * @param payload
     * @return
     */
    public Future<ScheduleResponse> scheduleRepeating(final long interval, final UUID id, final byte[] payload) {
        final IntervalScheduleRequest intervalScheduleRequest = new IntervalScheduleRequest(getModule(), getQueueName(), id, payload, interval);
        return send(intervalScheduleRequest);
    }
}
