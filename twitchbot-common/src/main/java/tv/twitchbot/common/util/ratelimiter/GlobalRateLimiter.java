package tv.twitchbot.common.util.ratelimiter;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 10/18/2016.
 */
public class GlobalRateLimiter implements RateLimiter {
    private ScheduledExecutorService scheduledExecutorService;
    private int interval;
    private InterProcessSemaphoreV2 semaphore;

    public GlobalRateLimiter(CuratorFramework framework, ScheduledExecutorService scheduledExecutorService, String name, int threshold, int interval) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.interval = interval;
        semaphore = new InterProcessSemaphoreV2(framework, "/global_rate_limiter/" + name, threshold);
    }

    @Override
    public void submit(Runnable task) {
        scheduledExecutorService.submit(() -> {
            try {
                submitAndWait(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void submitAndWait(Runnable task) throws InterruptedException {
        Lease lease;
        try {
            lease = semaphore.acquire();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        run(task, lease);
    }

    private void run(Runnable task, Lease lease) {
        try {
            task.run();
        } finally {
            scheduleRelease(lease);
        }
    }

    private void scheduleRelease(Lease lease) {
        scheduledExecutorService.schedule(() -> semaphore.returnLease(lease), interval, TimeUnit.SECONDS);
    }
}
