package tv.v1x1.common.modules;

import org.slf4j.MDC;
import tv.v1x1.common.dto.messages.Message;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/5/16.
 */
public abstract class ThreadedModule<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration, W extends ChannelConfiguration> extends Module<T, U, V, W> {
    private final ExecutorService executorService;

    protected ThreadedModule() {
        executorService = new ThreadPoolExecutor(5, 100, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));
    }

    protected ThreadedModule(final int count) {
        executorService = new ThreadPoolExecutor(count, count, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));
    }

    protected ThreadedModule(final int count, final int queueDepth) {
        executorService = new ThreadPoolExecutor(count, count, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueDepth));
    }

    private void processMessageWrapper(final Message message) {
        try {
            processMessage(message);
        } catch(final Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected abstract void processMessage(Message message);

    @Override
    protected void handle(final Message message) {
        final Map<String, String> contextMap = MDC.getCopyOfContextMap();
        executorService.submit(() -> {
            final Map<String, String> oldContextMap = MDC.getCopyOfContextMap();
            MDC.setContextMap(contextMap);
            try {
                processMessageWrapper(message);
            } finally {
                MDC.setContextMap(oldContextMap);
            }
        });
    }

    @Override
    protected void shutdown() {
        executorService.shutdown();
    }
}
