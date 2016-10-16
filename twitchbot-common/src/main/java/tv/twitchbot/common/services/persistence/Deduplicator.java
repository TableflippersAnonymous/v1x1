package tv.twitchbot.common.services.persistence;

import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;

import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/16/2016.
 */
public class Deduplicator {
    private RSetCache<byte[]> setCache;

    public Deduplicator(RedissonClient client, Module module) {
        this(client, "Module|" + module.getName());
    }

    public Deduplicator(RedissonClient client, String name) {
        setCache = client.getSetCache("Deduplicator|" + name, ByteArrayCodec.INSTANCE);
    }

    public boolean seenAndAdd(UUID uuid) {
        byte[] bytes = uuid.toProto().toByteArray();
        if(setCache.contains(bytes))
            return true;
        setCache.add(bytes, 5, TimeUnit.MINUTES);
        return false;
    }
}
