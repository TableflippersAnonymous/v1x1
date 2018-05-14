package tv.v1x1.common.util.ratelimiter;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/18/2016.
 */
public class GlobalRateLimiter implements RateLimiter {
    private final ScheduledExecutorService scheduledExecutorService;
    private final int interval;
    private final InterProcessSemaphoreV2 semaphore;

    public GlobalRateLimiter(final CuratorFramework framework, final ScheduledExecutorService scheduledExecutorService, final String name, final int threshold, final int interval) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.interval = interval;
        semaphore = new InterProcessSemaphoreV2(framework, "/global_rate_limiter/" + name, threshold);
    }

    @Override
    public void submit(final Runnable task) {
        scheduledExecutorService.submit(() -> submitAndWait(task));
    }

    @Override
    public void submitAndWait(final Runnable task) {
        final Lease lease;
        try {
            lease = semaphore.acquire();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        run(task, lease);
    }

    private void run(final Runnable task, final Lease lease) {
        try {
            task.run();
        } finally {
            scheduleRelease(lease);
        }
    }

    private void scheduleRelease(final Lease lease) {
        scheduledExecutorService.schedule(() -> semaphore.returnLease(lease), interval, TimeUnit.SECONDS);
    }
}
