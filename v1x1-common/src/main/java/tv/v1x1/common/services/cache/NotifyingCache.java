package tv.v1x1.common.services.cache;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import tv.v1x1.common.util.data.CompositeKey;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by naomi on 1/21/2017.
 */
public class NotifyingCache<V> implements SharedCache<V> {
    private final RTopic<byte[]> topic;
    private final int listenerId;
    private final SharedCache<V> cache;

    public NotifyingCache(final RedissonClient redisson, final String name, final SharedCache<V> cache) {
        this.topic = redisson.getTopic("Common|NotifyingCache|" + name, ByteArrayCodec.INSTANCE);
        this.listenerId = this.topic.addListener((channel, msg) -> {
            final byte[][] keys = CompositeKey.getKeys(msg);
            if(keys.length < 1)
                throw new IllegalArgumentException("Invalid CompositeKey found.");
            cache.invalidate(keys[0]);
        });
        this.cache = cache;
    }

    @Override
    public V get(final byte[] key) throws ExecutionException {
        return cache.get(key);
    }

    @Override
    public void put(final byte[] key, final V value) {
        cache.put(key, value);
        topic.publish(CompositeKey.makeKey(key));
    }

    @Override
    public void invalidate(final byte[] key) {
        cache.invalidate(key);
        topic.publish(CompositeKey.makeKey(key));
    }

    @Override
    public void close() throws IOException {
        topic.removeListener(listenerId);
    }
}
