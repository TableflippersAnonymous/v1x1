package tv.v1x1.common.services.cache;

import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

/**
 * Created by naomi on 1/21/2017.
 */
public class LocalCache<K, V> implements SharedCache<K, V> {
    private final LoadingCache<K, V> cache;

    public LocalCache(final LoadingCache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public V get(final K key) throws ExecutionException {
        return cache.get(key);
    }

    @Override
    public void put(final K key, final V value) {
        cache.put(key, value);
    }

    @Override
    public void invalidate(final K key) {
        cache.invalidate(key);
    }

    @Override
    public void close() {

    }
}
