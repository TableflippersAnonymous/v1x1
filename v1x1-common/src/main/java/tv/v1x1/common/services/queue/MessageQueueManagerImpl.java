package tv.v1x1.common.services.queue;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;

/**
 * Created by cobi on 10/6/2016.
 */
@Singleton
public class MessageQueueManagerImpl implements MessageQueueManager {
    private final RedissonClient redissonClient;

    @Inject
    public MessageQueueManagerImpl(final RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public MessageQueue forName(final String name) {
        return new MessageQueueImpl(redissonClient.getBlockingQueue(name, ByteArrayCodec.INSTANCE));
    }
}
