package tv.v1x1.modules.core.scheduler;

import com.datastax.driver.mapping.MappingManager;
import org.redisson.api.RedissonClient;
import tv.v1x1.common.dto.messages.requests.CronScheduleRequest;
import tv.v1x1.common.dto.messages.requests.DelayScheduleRequest;
import tv.v1x1.common.dto.messages.requests.IntervalScheduleRequest;
import tv.v1x1.common.dto.messages.requests.ScheduleRequest;
import tv.v1x1.common.dto.messages.responses.ScheduleResponse;
import tv.v1x1.common.modules.GlobalConfiguration;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.modules.ModuleSettings;
import tv.v1x1.common.modules.TenantConfiguration;
import tv.v1x1.common.rpc.services.ThreadedService;
import tv.v1x1.common.services.queue.MessageQueue;
import tv.v1x1.modules.core.scheduler.dao.DAOCronSchedule;
import tv.v1x1.modules.core.scheduler.dao.DAOIntervalSchedule;
import tv.v1x1.modules.core.scheduler.schedulers.CronScheduler;
import tv.v1x1.modules.core.scheduler.schedulers.DelayScheduler;
import tv.v1x1.modules.core.scheduler.schedulers.IntervalScheduler;

import java.util.Date;

/**
 * Created by cobi on 10/22/2016.
 */
public class SchedulerService extends ThreadedService<ScheduleRequest, ScheduleResponse> {
    private final CronScheduler cronScheduler;
    private final DelayScheduler delayScheduler;
    private final IntervalScheduler intervalScheduler;

    public SchedulerService(final Module<?, ?, ?, ?> module) {
        super(module, "Scheduler", ScheduleRequest.class);
        final MessageQueue messageQueue = module.getMessageQueueManager().forName(Module.getMainQueueForModule(new tv.v1x1.common.dto.core.Module("event_router")));
        final MappingManager mappingManager = module.getMappingManager();
        final RedissonClient client = module.getRedisson();
        delayScheduler = new DelayScheduler(client, messageQueue, module.toDto());
        cronScheduler = new CronScheduler(delayScheduler, new DAOCronSchedule(mappingManager), client);
        intervalScheduler = new IntervalScheduler(delayScheduler, new DAOIntervalSchedule(mappingManager), client);
    }

    @Override
    public void start() {
        delayScheduler.start();
        cronScheduler.start();
        intervalScheduler.start();
        super.start();
    }

    @Override
    protected ScheduleResponse call(final ScheduleRequest request) {
        if(request instanceof CronScheduleRequest)
            return callInternal((CronScheduleRequest) request);
        if(request instanceof DelayScheduleRequest)
            return callInternal((DelayScheduleRequest) request);
        if(request instanceof IntervalScheduleRequest)
            return callInternal((IntervalScheduleRequest) request);
        throw new IllegalArgumentException("Unknown Schedule Request type: " + request.getClass().getCanonicalName());
    }

    private ScheduleResponse callInternal(final CronScheduleRequest request) {
        cronScheduler.schedule(request.getMinute(), request.getHour(), request.getDayOfMonth(), request.getMonth(),
                request.getDayOfWeek(), request.getFrom(), request.getScheduleId(), request.getPayload());
        return new ScheduleResponse(getModule().toDto(), request.getMessageId());
    }

    private ScheduleResponse callInternal(final DelayScheduleRequest request) {
        delayScheduler.schedule(new Date().getTime() + request.getDelay(), request.getFrom(), request.getScheduleId(), request.getPayload());
        return new ScheduleResponse(getModule().toDto(), request.getMessageId());
    }

    private ScheduleResponse callInternal(final IntervalScheduleRequest request) {
        intervalScheduler.schedule(request.getDelay(), request.getFrom(), request.getScheduleId(), request.getPayload());
        return new ScheduleResponse(getModule().toDto(), request.getMessageId());
    }

    @Override
    public void shutdown() {
        super.shutdown();
        cronScheduler.shutdown();
        intervalScheduler.shutdown();
        delayScheduler.shutdown();
    }
}
