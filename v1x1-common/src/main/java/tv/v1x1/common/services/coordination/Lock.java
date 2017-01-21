package tv.v1x1.common.services.coordination;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.Callable;

/**
 * Created by naomi on 1/21/2017.
 */
public class Lock {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final InterProcessMutex interProcessMutex;
    private final String name;

    public Lock(final CuratorFramework client, final String name) {
        this.interProcessMutex = new InterProcessMutex(client, "/v1x1/locks/" + name);
        this.name = name;
    }

    public <V> V call(final Callable<V> callable) throws Exception {
        try {
            try {
                interProcessMutex.acquire();
            } catch (final Exception e) {
                LOG.warn("Got exception acquiring lock: {}", name, e);
                throw e;
            }
            return callable.call();
        } catch(final Exception e) {
            LOG.warn("Got exception executing lock: {}", name, e);
            throw e;
        } finally {
            try {
                interProcessMutex.release();
            } catch(final Exception e) {
                LOG.warn("Got exception releasing lock: {}", name, e);
                throw new IllegalStateException(e);
            }
        }
    }

    public void run(final Runnable runnable) throws Exception {
        call(() -> {
            runnable.run();
            return null;
        });
    }
}
