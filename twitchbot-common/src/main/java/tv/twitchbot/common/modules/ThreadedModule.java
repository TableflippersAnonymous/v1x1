package tv.twitchbot.common.modules;

import tv.twitchbot.common.dto.messages.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 10/5/16.
 */
public abstract class ThreadedModule<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration> extends Module<T, U, V> {
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
        executorService.submit(() -> processMessageWrapper(message));
    }

    @Override
    protected void shutdown() {
        executorService.shutdown();
    }
}
