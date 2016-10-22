package tv.v1x1.common.util.ratelimiter;

/**
 * Created by cobi on 10/18/2016.
 */
public interface RateLimiter {
    void submit(Runnable task);
    void submitAndWait(Runnable task) throws InterruptedException;
}
