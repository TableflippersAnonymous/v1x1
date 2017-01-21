package tv.v1x1.common.services.cache;

import java.io.Closeable;
import java.util.concurrent.ExecutionException;

/**
 * Created by naomi on 10/5/16.
 */
public interface SharedCache<K, V> extends Closeable {
    V get(final K key) throws ExecutionException;
    void put(final K key, final V value);
    void invalidate(final K key);
}
