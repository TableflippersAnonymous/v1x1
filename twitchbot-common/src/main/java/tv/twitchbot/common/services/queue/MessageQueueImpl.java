package tv.twitchbot.common.services.queue;

import com.google.protobuf.InvalidProtocolBufferException;
import org.redisson.api.RBlockingQueue;
import tv.twitchbot.common.dto.messages.Message;

/**
 * Created by naomi on 10/5/16.
 */
public class MessageQueueImpl implements MessageQueue {

    private RBlockingQueue<byte[]> blockingQueue;

    public MessageQueueImpl(RBlockingQueue<byte[]> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public Message get() throws InterruptedException, InvalidProtocolBufferException {
        return Message.fromBytes(blockingQueue.take());
    }

    @Override
    public void add(Message message) {
        blockingQueue.add(message.toBytes());
    }
}
