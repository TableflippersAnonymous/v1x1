package tv.twitchbot.common.services.stats;

/**
 * Created by cobi on 10/12/2016.
 */
public class NoopStatsCollector implements StatsCollector {

    @Override
    public void track(Throwable throwable) {
        //TODO: Stub.
    }

    @Override
    public void event(String eventName) {
        //TODO: Stub.
    }

    @Override
    public void timedEvent(String eventName, long timeMs) {
        //TODO: Stub.
    }

    @Override
    public void timedEvent(String eventName, Runnable runnable) {
        //TODO: Stub.
        runnable.run();
    }

    @Override
    public void set(String stat, long value) {
        //TODO: Stub.
    }
}
