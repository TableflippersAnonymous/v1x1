package tv.twitchbot.common.services.persistence;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import tv.twitchbot.common.dto.core.Module;

import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 10/7/2016.
 */
public class TemporaryKeyValueStoreImpl implements KeyValueStore {
    private RMapCache<byte[], byte[]> mapCache;

    public TemporaryKeyValueStoreImpl(RedissonClient client, Module module) {
        mapCache = client.getMapCache("TemporaryKeyValueStore|" + module.getName(), ByteArrayCodec.INSTANCE);
    }

    public TemporaryKeyValueStoreImpl(RedissonClient client) {
        mapCache = client.getMapCache("TemporaryKeyValueStore", ByteArrayCodec.INSTANCE);
    }

    @Override
    public void put(byte[] key, byte[] value) {
        mapCache.put(key, value, 7, TimeUnit.DAYS);
    }

    @Override
    public byte[] get(byte[] key) {
        return mapCache.get(key);
    }

    @Override
    public void delete(byte[] key) {
        mapCache.remove(key);
    }
}
