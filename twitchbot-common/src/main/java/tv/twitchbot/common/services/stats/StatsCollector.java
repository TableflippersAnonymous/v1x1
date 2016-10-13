package tv.twitchbot.common.services.stats;

/**
 * Created by cobi on 10/12/2016.
 */
public interface StatsCollector {
    void track(Throwable throwable);
    void event(String eventName);
    void timedEvent(String eventName, long timeMs);
    void timedEvent(String eventName, Runnable runnable);
    void set(String stat, long value);
}
