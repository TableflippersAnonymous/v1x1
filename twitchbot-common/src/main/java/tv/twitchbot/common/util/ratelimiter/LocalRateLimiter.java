package tv.twitchbot.common.util.ratelimiter;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/12/2016.
 */
public class LocalRateLimiter implements RateLimiter {
    private ScheduledExecutorService scheduledExecutorService;
    private int interval;
    private Semaphore semaphore;

    public LocalRateLimiter(ScheduledExecutorService scheduledExecutorService, int threshold, int interval) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.interval = interval;
        semaphore = new Semaphore(threshold, true);
    }

    @Override
    public void submit(Runnable task) {
        if(semaphore.tryAcquire()) {
            run(task);
        } else {
            scheduledExecutorService.submit(() -> {
                try {
                    submitAndWait(task);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void submitAndWait(Runnable task) throws InterruptedException {
        semaphore.acquire();
        run(task);
    }

    private void run(Runnable task) {
        task.run();
        scheduleRelease();
    }

    private void scheduleRelease() {
        scheduledExecutorService.schedule(() -> semaphore.release(), interval, TimeUnit.SECONDS);
    }
}
