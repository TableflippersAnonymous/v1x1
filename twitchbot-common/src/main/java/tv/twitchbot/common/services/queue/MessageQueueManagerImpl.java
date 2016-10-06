package tv.twitchbot.common.services.queue;

import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;

/**
 * Created by cobi on 10/6/2016.
 */
public class MessageQueueManagerImpl implements MessageQueueManager {
    private RedissonClient redissonClient;

    public MessageQueueManagerImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public MessageQueue forName(String name) {
        return new MessageQueueImpl(redissonClient.getBlockingQueue(name, ByteArrayCodec.INSTANCE));
    }
}
