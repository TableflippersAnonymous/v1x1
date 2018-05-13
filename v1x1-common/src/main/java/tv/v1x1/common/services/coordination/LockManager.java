package tv.v1x1.common.services.coordination;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.curator.framework.CuratorFramework;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 1/21/2017.
 */
@Singleton
public class LockManager {
    private final LoadingCache<String, Lock> cache;

    @Inject
    public LockManager(final CuratorFramework curator) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Lock>() {
                    @Override
                    public Lock load(final String key) {
                        return new Lock(curator, key);
                    }
                });
    }

    public Lock get(final String name) {
        try {
            return cache.get(name);
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
