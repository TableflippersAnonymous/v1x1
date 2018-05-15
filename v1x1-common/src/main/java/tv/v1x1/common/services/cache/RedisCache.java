package tv.v1x1.common.services.cache;

import com.google.common.cache.CacheLoader;
import com.google.common.io.BaseEncoding;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import tv.v1x1.common.services.coordination.LockManager;

import javax.cache.integration.CacheLoaderException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 1/21/2017.
 */
public class RedisCache implements SharedCache<byte[], byte[]> {
    private final RMapCache<byte[], byte[]> cache;
    private final long ttl;
    private final TimeUnit timeUnit;
    private final CacheLoader<byte[], byte[]> cacheLoader;
    private final LockManager lockManager;
    private final String name;

    public RedisCache(final RedissonClient redisson, final String name, final long ttl, final TimeUnit timeUnit, final CacheLoader<byte[], byte[]> cacheLoader, final LockManager lockManager) {
        this.cache = redisson.getMapCache("Common|RedisCache|" + name, ByteArrayCodec.INSTANCE);
        this.ttl = ttl;
        this.timeUnit = timeUnit;
        this.cacheLoader = cacheLoader;
        this.lockManager = lockManager;
        this.name = name;
    }

    @Override
    public byte[] get(final byte[] key) throws ExecutionException {
        final byte[] value = cache.get(key);
        if(value != null)
            return value;
        try {
            return lockManager.get("common/redis_cache/" + name + "/" + BaseEncoding.base64Url().encode(key)).call(() -> {
                final byte[] tryValue = cache.get(key);
                if(tryValue != null)
                    return tryValue;
                final byte[] value1 = cacheLoader.load(key);
                if(value1 == null)
                    throw new CacheLoaderException("Cache Loader returned null value.");
                put(key, value1);
                return value1;
            });
        } catch (final Exception e) {
            throw new ExecutionException(e);
        }
    }

    @Override
    public void put(final byte[] key, final byte[] value) {
        cache.fastPut(key, value, ttl, timeUnit);
    }

    @Override
    public void invalidate(final byte[] key) {
        cache.remove(key);
    }

    @Override
    public void close() {
        /* No action */
    }
}
