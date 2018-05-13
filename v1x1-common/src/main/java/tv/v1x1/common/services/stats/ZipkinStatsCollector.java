package tv.v1x1.common.services.stats;

import brave.Span;
import brave.Tracer;
import com.google.inject.Inject;

/**
 * Created by cobi on 4/15/2017.
 */
public class ZipkinStatsCollector extends NoopStatsCollector {
    private final Tracer tracer;

    @Inject
    public ZipkinStatsCollector(final Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void timedEvent(final String eventName, final Runnable runnable) {
        final Span span = tracer.newTrace().name(eventName).start();
        try {
            runnable.run();
        } finally {
            span.finish();
        }
    }
}
