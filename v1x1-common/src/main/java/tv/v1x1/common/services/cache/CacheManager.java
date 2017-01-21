package tv.v1x1.common.services.cache;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.redisson.api.RedissonClient;
import tv.v1x1.common.services.coordination.LockManager;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by cobi on 1/21/2017.
 */
public class CacheManager {
    private final RedissonClient redissonClient;
    private final LockManager lockManager;

    public CacheManager(final LockManager lockManager, final RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        this.lockManager = lockManager;
    }

    public <K, V> LocalCache<K, V> localCache(final LoadingCache<K, V> cache) {
        return new LocalCache<>(cache);
    }

    public <V> NotifyingCache<V> notifyingCache(final String name, final SharedCache<byte[], V> cache) {
        return new NotifyingCache<V>(redissonClient, name, cache);
    }

    public RedisCache redisCache(final String name, final long ttl, final TimeUnit timeUnit, final CacheLoader<byte[], byte[]> cacheLoader) {
        return new RedisCache(redissonClient, name, ttl, timeUnit, cacheLoader, lockManager);
    }

    public <K, V> CodecCache<K, V> codec(final SharedCache<byte[], byte[]> cache, final CodecCache.Codec<K> keyCodec, final CodecCache.Codec<V> valCodec) {
        return new CodecCache<>(cache, keyCodec, valCodec);
    }

    public <K, V> CodecCache<K, V> codec(final SharedCache<byte[], byte[]> cache,
                                         final Function<K, byte[]> keyEncode, final Function<byte[], K> keyDecode,
                                         final Function<V, byte[]> valEncode, final Function<byte[], V> valDecode) {
        return codec(cache, new LambdaCodec<>(keyEncode, keyDecode), new LambdaCodec<>(valEncode, valDecode));
    }
}
