package tv.twitchbot.common.util.ratelimiter;

/**
 * Created by naomi on 10/18/2016.
 */
public interface RateLimiter {
    void submit(Runnable task);
    void submitAndWait(Runnable task) throws InterruptedException;
}
