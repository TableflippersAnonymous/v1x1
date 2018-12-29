package tv.v1x1.common.util.ratelimiter;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 10/12/2016.
 */
public class LocalRateLimiter implements RateLimiter {
    private final ScheduledExecutorService scheduledExecutorService;
    private final int interval;
    private final Semaphore semaphore;

    public LocalRateLimiter(final ScheduledExecutorService scheduledExecutorService, final int threshold, final int interval) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.interval = interval;
        semaphore = new Semaphore(threshold, true);
    }

    @Override
    public void submit(final Runnable task) {
        if(semaphore.tryAcquire()) {
            run(task);
        } else {
            scheduledExecutorService.submit(() -> {
                try {
                    submitAndWait(task);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void submitAndWait(final Runnable task) throws InterruptedException {
        semaphore.acquire();
        run(task);
    }

    private void run(final Runnable task) {
        try {
            task.run();
        } finally {
            scheduleRelease();
        }
    }

    private void scheduleRelease() {
        scheduledExecutorService.schedule((Runnable) semaphore::release, interval, TimeUnit.SECONDS);
    }
}
