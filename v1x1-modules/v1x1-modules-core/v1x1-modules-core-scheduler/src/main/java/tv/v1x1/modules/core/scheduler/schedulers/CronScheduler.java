package tv.v1x1.modules.core.scheduler.schedulers;

import com.google.common.primitives.Longs;
import com.google.protobuf.InvalidProtocolBufferException;
import org.joda.time.DateTime;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.proto.core.ModuleOuterClass;
import tv.v1x1.modules.core.scheduler.dao.DAOCronSchedule;
import tv.v1x1.modules.core.scheduler.dto.CronSchedule;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/22/2016.
 */
public class CronScheduler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ScheduledExecutorService mainLoop = Executors.newSingleThreadScheduledExecutor();
    private final DelayScheduler delayScheduler;
    private final DAOCronSchedule daoCronSchedule;
    private final RMapCache<byte[], byte[]> lastRuns;
    private final RLock lock;

    public CronScheduler(final DelayScheduler delayScheduler, final DAOCronSchedule daoCronSchedule, final RedissonClient redissonClient) {
        this.delayScheduler = delayScheduler;
        this.daoCronSchedule = daoCronSchedule;
        lastRuns = redissonClient.getMapCache("Modules|Core|Scheduler|Cron|LastRuns", ByteArrayCodec.INSTANCE);
        lock = redissonClient.getLock("Modules|Core|Scheduler|Cron|Lock");
    }

    public void schedule(final Set<Integer> minute, final Set<Integer> hour, final Set<Integer> dayOfMonth, final Set<Integer> month,
                         final Set<Integer> dayOfWeek, final Module module, final UUID id, final byte[] payload) {
        final CronSchedule cronSchedule = new CronSchedule(id.getValue(), module.toProto().toByteArray(), payload, minute, hour, dayOfMonth, month, dayOfWeek);
        if(minute.isEmpty() && hour.isEmpty() && dayOfMonth.isEmpty() && month.isEmpty() && dayOfWeek.isEmpty()) {
            daoCronSchedule.delete(cronSchedule);
            delayScheduler.cancel(id);
        } else {
            try {
                processTask(cronSchedule);
            } catch (InvalidProtocolBufferException e) {
                LOG.warn("Exception caught while processing schedule {}", cronSchedule.getId(), e);
            }
            daoCronSchedule.save(cronSchedule);
        }
    }

    public void start() {
        mainLoop.scheduleAtFixedRate(this, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        try {
            final Iterable<CronSchedule> cronSchedules = daoCronSchedule.all();
            for (final CronSchedule schedule : cronSchedules)
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

    private void processTask(final CronSchedule cronSchedule) throws InvalidProtocolBufferException {
        LOG.debug("Processing CronSchedule: {}", cronSchedule.getId());
        final long now = new Date().getTime();
        final long originalLastRunTime = getLastRun(cronSchedule.getId());
        if(originalLastRunTime == -1) {
            updateLastRun(cronSchedule.getId(), now);
        }

        long lastRunTime = getLastRun(cronSchedule.getId());
        long nextRunTime = getNextRunTime(cronSchedule, lastRunTime, now + 600000);
        while(nextRunTime < now + 600000) {
            updateLastRun(cronSchedule.getId(), nextRunTime);
            scheduleTask(cronSchedule, nextRunTime);
            lastRunTime = getLastRun(cronSchedule.getId());
            nextRunTime = getNextRunTime(cronSchedule, lastRunTime, now + 600000);
        }
    }

    private static long getNextRunTime(final CronSchedule cronSchedule, final long timestamp, final long limit) {
        for(long ts = timestamp; ts < limit; ts += 1000) {
            if(shouldRunAt(cronSchedule, ts))
                return ts;
        }
        return Long.MAX_VALUE;
    }

    private static boolean shouldRunAt(final CronSchedule cronSchedule, final long timestamp) {
        final DateTime dateTime = new DateTime(timestamp);
        return cronEntryEquals(cronSchedule.getMinute(), dateTime.getMinuteOfHour())
                && cronEntryEquals(cronSchedule.getHour(), dateTime.getHourOfDay())
                && cronEntryEquals(cronSchedule.getDayOfMonth(), dateTime.getDayOfMonth())
                && cronEntryEquals(cronSchedule.getMonth(), dateTime.getMonthOfYear())
                && cronEntryEquals(cronSchedule.getDayOfWeek(), dateTime.getDayOfWeek());
    }

    private static boolean cronEntryEquals(final Collection<Integer> set, final Integer val) {
        if(set.contains(val))
            return true;
        for(final Integer entry : set)
            if(entry < 0 && val % (-entry) == 0)
                return true;
        return false;
    }

    private void scheduleTask(final CronSchedule cronSchedule, final long timestamp) throws InvalidProtocolBufferException {
        delayScheduler.schedule(timestamp, Module.fromProto(ModuleOuterClass.Module.parseFrom(cronSchedule.getModuleBytes())),
                new UUID(cronSchedule.getId()), cronSchedule.getPayloadBytes());
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
