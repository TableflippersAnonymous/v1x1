package tv.v1x1.common.services.persistence;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.util.data.FixedRMapCache;

import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 10/7/2016.
 */
public class TemporaryKeyValueStoreImpl implements KeyValueStore {
    private final RMapCache<byte[], byte[]> mapCache;

    public TemporaryKeyValueStoreImpl(final RedissonClient client, final Module module) {
        mapCache = new FixedRMapCache(client.getMapCache("TemporaryKeyValueStore|" + module.getName(), ByteArrayCodec.INSTANCE));
    }

    public TemporaryKeyValueStoreImpl(final RedissonClient client) {
        mapCache = new FixedRMapCache(client.getMapCache("TemporaryKeyValueStore", ByteArrayCodec.INSTANCE));
    }

    @Override
    public void put(final byte[] key, final byte[] value) {
        mapCache.fastPut(key, value, 7, TimeUnit.DAYS);
    }

    @Override
    public byte[] get(final byte[] key) {
        final byte[] value = mapCache.get(key);
        if (value == null || value.length == 0)
            return null;
        return value;
    }

    @Override
    public void delete(final byte[] key) {
        mapCache.fastRemove(key);
    }
}
