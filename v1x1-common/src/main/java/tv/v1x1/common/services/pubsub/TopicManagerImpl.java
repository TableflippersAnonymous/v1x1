package tv.v1x1.common.services.pubsub;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.protobuf.InvalidProtocolBufferException;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.messages.Message;
import tv.v1x1.common.dto.messages.PubSubMessage;

import java.lang.invoke.MethodHandles;

/**
 * Created by cobi on 4/30/2017.
 */
@Singleton
public class TopicManagerImpl implements TopicManager {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RedissonClient redissonClient;

    @Inject
    public TopicManagerImpl(final RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void publish(final String topic, final PubSubMessage pubSubMessage) {
        final RTopic<byte[]> rTopic = redissonClient.getTopic("Topic|" + topic, ByteArrayCodec.INSTANCE);
        rTopic.publish(pubSubMessage.toProto().toByteArray());
    }

    @Override
    public int addListener(final String topic, final TopicListener topicListener) {
        final RTopic<byte[]> rTopic = redissonClient.getTopic("Topic|" + topic, ByteArrayCodec.INSTANCE);
        return rTopic.addListener((channel, msg) -> {
            try {
                final Message message = Message.fromBytes(msg);
                if(message instanceof PubSubMessage)
                    topicListener.onMessage((PubSubMessage) message);
                else
                    LOG.warn("Got message of type {} on topic {}, expecting PubSubMessage", message.getClass().getCanonicalName(), topic);
            } catch (final InvalidProtocolBufferException e) {
                LOG.error("Got exception parsing topic message on topic {}", topic, e);
            }
        });
    }

    @Override
    public void removeListener(final String topic, final int listenerId) {
        final RTopic<byte[]> rTopic = redissonClient.getTopic("Topic|" + topic, ByteArrayCodec.INSTANCE);
        rTopic.removeListener(listenerId);
    }
}
