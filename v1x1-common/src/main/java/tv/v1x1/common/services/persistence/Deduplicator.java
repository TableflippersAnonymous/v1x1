package tv.v1x1.common.services.persistence;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;

import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/16/2016.
 */
@Singleton
public class Deduplicator {
    private final RSetCache<byte[]> setCache;

    @Inject
    public Deduplicator(final RedissonClient client, final Module module) {
        this(client, "Module|" + module.getName());
    }

    public Deduplicator(final RedissonClient client, final String name) {
        setCache = client.getSetCache("Deduplicator|" + name, ByteArrayCodec.INSTANCE);
    }

    public boolean seenAndAdd(final UUID uuid) {
        final byte[] bytes = uuid.toProto().toByteArray();
        return !setCache.add(bytes, 5, TimeUnit.MINUTES);
    }
}
