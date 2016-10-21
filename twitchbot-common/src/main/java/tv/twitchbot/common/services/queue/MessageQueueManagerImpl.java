package tv.twitchbot.common.services.queue;

import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;

/**
 * Created by naomi on 10/6/2016.
 */
public class MessageQueueManagerImpl implements MessageQueueManager {
    private final RedissonClient redissonClient;

    public MessageQueueManagerImpl(final RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public MessageQueue forName(final String name) {
        return new MessageQueueImpl(redissonClient.getBlockingQueue(name, ByteArrayCodec.INSTANCE));
    }
}
