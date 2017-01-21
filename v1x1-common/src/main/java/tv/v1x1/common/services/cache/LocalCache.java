package tv.v1x1.common.services.cache;

import com.google.common.cache.LoadingCache;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by cobi on 1/21/2017.
 */
public class LocalCache<V> implements SharedCache<V> {
    private final LoadingCache<byte[], V> cache;

    public LocalCache(final LoadingCache<byte[], V> cache) {
        this.cache = cache;
    }

    @Override
    public V get(final byte[] key) throws ExecutionException {
        return cache.get(key);
    }

    @Override
    public void put(final byte[] key, final V value) {
        cache.put(key, value);
    }

    @Override
    public void invalidate(final byte[] key) {
        cache.invalidate(key);
    }

    @Override
    public void close() throws IOException {

    }
}
