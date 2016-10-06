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
    private ExecutorService executorService;

    protected ThreadedModule() {
        executorService = new ThreadPoolExecutor(5, 100, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));
    }

    protected ThreadedModule(int count) {
        executorService = new ThreadPoolExecutor(count, count, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));
    }

    protected ThreadedModule(int count, int queueDepth) {
        executorService = new ThreadPoolExecutor(count, count, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueDepth));
    }

    protected abstract void processMessage(Message message);

    @Override
    protected void handle(final Message message) {
        executorService.submit(() -> processMessage(message));
    }

    @Override
    protected void shutdown() {
        executorService.shutdown();
    }
}
