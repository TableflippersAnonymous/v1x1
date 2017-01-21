package tv.v1x1.common.services.cache;

import java.io.Closeable;
import java.util.concurrent.ExecutionException;

/**
 * Created by naomi on 10/5/16.
 */
public interface SharedCache<V> extends Closeable {
    V get(final byte[] key) throws ExecutionException;
    void put(final byte[] key, final V value);
    void invalidate(final byte[] key);
}
