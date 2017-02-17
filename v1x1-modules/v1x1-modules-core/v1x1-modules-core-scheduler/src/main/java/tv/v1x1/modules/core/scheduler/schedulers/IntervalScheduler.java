package tv.v1x1.modules.core.scheduler.schedulers;

import com.google.common.primitives.Longs;
import com.google.protobuf.InvalidProtocolBufferException;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.proto.core.ModuleOuterClass;
import tv.v1x1.modules.core.scheduler.dao.DAOIntervalSchedule;
import tv.v1x1.modules.core.scheduler.dto.IntervalSchedule;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/22/2016.
 */
public class IntervalScheduler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ScheduledExecutorService mainLoop = Executors.newSingleThreadScheduledExecutor();
    private final DelayScheduler delayScheduler;
    private final DAOIntervalSchedule daoIntervalSchedule;
    private final RMapCache<byte[], byte[]> lastRuns;
    private final RLock lock;

    public IntervalScheduler(final DelayScheduler delayScheduler, final DAOIntervalSchedule daoIntervalSchedule, final RedissonClient redissonClient) {
        this.delayScheduler = delayScheduler;
        this.daoIntervalSchedule = daoIntervalSchedule;
        lastRuns = redissonClient.getMapCache("Modules|Core|Scheduler|Interval|LastRuns", ByteArrayCodec.INSTANCE);
        lock = redissonClient.getLock("Modules|Core|Scheduler|Interval|Lock");
    }

    public void schedule(final long interval, final Module module, final UUID id, final byte[] payload) {
        final IntervalSchedule intervalSchedule = new IntervalSchedule(id.getValue(), module.toProto().toByteArray(), payload, interval);
        if(interval == -1) {
            daoIntervalSchedule.delete(intervalSchedule);
            delayScheduler.cancel(id);
        } else {
            try {
                /* If we already have a schedule by this ID, cancel the existing ones, and update lastRun */
                final IntervalSchedule oldIntervalSchedule = daoIntervalSchedule.get(id.getValue());
                final long oldNextRun = delayScheduler.cancel(id);
                if(oldNextRun != -1 && oldIntervalSchedule != null) {
                    final long lastRun = oldNextRun - oldIntervalSchedule.getInterval();
                    updateLastRun(id.getValue(), lastRun);
                }
                processTask(intervalSchedule);
            } catch (InvalidProtocolBufferException e) {
                LOG.warn("Exception caught while processing schedule {}", intervalSchedule.getId(), e);
            }
            daoIntervalSchedule.save(intervalSchedule);
        }
    }

    public void start() {
        mainLoop.scheduleAtFixedRate(this, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        try {
            final Iterable<IntervalSchedule> intervalSchedules = daoIntervalSchedule.all();
            for (final IntervalSchedule schedule : intervalSchedules)
                try {
                    lock.lock(5, TimeUnit.SECONDS);
                    processTask(schedule);
                } catch (final Exception e) {
                    LOG.warn("Exception caught while processing schedule {}", schedule.getId(), e);
                } finally {
                    lock.unlock();
                }
        } catch(Exception e) {
            LOG.warn("Exception caught while processing schedules", e);
        }
    }

    private void processTask(final IntervalSchedule intervalSchedule) throws InvalidProtocolBufferException {
        LOG.debug("Processing IntervalSchedule: {}", intervalSchedule.getId());
        final long now = new Date().getTime();
        final long originalLastRunTime = getLastRun(intervalSchedule.getId());
        if(originalLastRunTime == -1) {
            final long lastRunTime = updateLastRun(intervalSchedule.getId(), now);
            /* if(lastRunTime == -1) {
                scheduleTask(intervalSchedule, now);
            } */
        }

        long lastRunTime = getLastRun(intervalSchedule.getId());
        while(lastRunTime + intervalSchedule.getInterval() < now + 300000) {
            updateLastRun(intervalSchedule.getId(), lastRunTime + intervalSchedule.getInterval());
            scheduleTask(intervalSchedule, lastRunTime + intervalSchedule.getInterval());
            lastRunTime = getLastRun(intervalSchedule.getId());
        }
    }

    private void scheduleTask(final IntervalSchedule intervalSchedule, final long timestamp) throws InvalidProtocolBufferException {
        delayScheduler.schedule(timestamp, Module.fromProto(ModuleOuterClass.Module.parseFrom(intervalSchedule.moduleBytes())),
                new UUID(intervalSchedule.getId()), intervalSchedule.payloadBytes());
    }

    private long getLastRun(final java.util.UUID id) {
        final byte[] lastRun = lastRuns.get(new UUID(id).toProto().toByteArray());
        if(lastRun == null)
            return -1;
        return Longs.fromByteArray(lastRun);
    }

    private long updateLastRun(final java.util.UUID id, final long newLastRun) {
        final byte[] newLastRunBytes = Longs.toByteArray(newLastRun);
        final byte[] lastRun = lastRuns.put(new UUID(id).toProto().toByteArray(), newLastRunBytes, 7, TimeUnit.DAYS);
        if(lastRun == null)
            return -1;
        return Longs.fromByteArray(lastRun);
    }

    public void shutdown() {
        mainLoop.shutdown();
    }
}
