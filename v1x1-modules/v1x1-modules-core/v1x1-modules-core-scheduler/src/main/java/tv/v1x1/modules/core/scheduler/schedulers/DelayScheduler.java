package tv.v1x1.modules.core.scheduler.schedulers;

import com.google.common.primitives.Longs;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.events.SchedulerNotifyEvent;
import tv.v1x1.common.dto.proto.core.ModuleOuterClass;
import tv.v1x1.common.dto.proto.core.UUIDOuterClass;
import tv.v1x1.common.services.queue.MessageQueue;
import tv.v1x1.common.util.data.CompositeKey;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/22/2016.
 */
public class DelayScheduler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RScoredSortedSet<byte[]> set;
    private final ScheduledExecutorService mainLoop = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final MessageQueue messageQueue;
    private final Module source;

    public DelayScheduler(final RedissonClient client, final MessageQueue messageQueue, final Module module) {
        set = client.getScoredSortedSet("Modules|Core|Scheduler|Delay", ByteArrayCodec.INSTANCE);
        this.messageQueue = messageQueue;
        this.source = module;
    }

    public void schedule(final long timestamp, final Module module, final UUID id, final byte[] payload) {
        set.add(timestamp, CompositeKey.makeKey(module.toProto().toByteArray(), id.toProto().toByteArray(), payload, Longs.toByteArray(timestamp)));
    }

    public long cancel(final UUID id) {
        long ret = -1;
        for(final byte[] schedule : set) {
            try {
                final byte[][] scheduleParts = CompositeKey.getKeys(schedule);
                final UUID scheduleId = UUID.fromProto(UUIDOuterClass.UUID.parseFrom(scheduleParts[1]));
                if(id.equals(scheduleId)) {
                    set.remove(schedule);
                    final long timestamp = Longs.fromByteArray(scheduleParts[3]);
                    if(ret == -1 || timestamp < ret)
                        ret = timestamp;
                }
            } catch(final Exception e) {
                LOG.warn("Exception caught while attempting to decode delay scheduler entry: {}", schedule, e);
            }
        }
        return ret;
    }

    public void start() {
        mainLoop.scheduleAtFixedRate(this, 0, 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        try {
            while (!set.isEmpty()) {
                final byte[] key = set.first();
                if (key == null)
                    break;
                try {
                    final byte[][] keyParts = CompositeKey.getKeys(key);
                    final Module module = Module.fromProto(ModuleOuterClass.Module.parseFrom(keyParts[0]));
                    final UUID id = UUID.fromProto(UUIDOuterClass.UUID.parseFrom(keyParts[1]));
                    final byte[] payload = keyParts[2];
                    final long timestamp = Longs.fromByteArray(keyParts[3]);
                    final long timeDifference = timestamp - new Date().getTime();
                    if (timeDifference > 0) {
                        if (timeDifference < 50)
                            mainLoop.schedule(this, timeDifference, TimeUnit.MILLISECONDS);
                        break;
                    }
                    LOG.debug("Processing {}", id.getValue());
                    if (set.remove(key))
                        messageQueue.add(new SchedulerNotifyEvent(source, module, id, payload));
                } catch (final Exception e) {
                    LOG.warn("Exception caught while attempting to decode delay scheduler entry: {}", key, e);
                    break;
                }
            }
        } catch (final Exception e) {
            LOG.warn("Exception caught while attempting to process entries.", e);
        }
    }

    public void shutdown() {
        mainLoop.shutdown();
        executorService.shutdown();
    }
}
