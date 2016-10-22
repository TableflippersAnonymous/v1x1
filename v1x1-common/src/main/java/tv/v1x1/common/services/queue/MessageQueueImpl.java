package tv.v1x1.common.services.queue;

import com.google.protobuf.InvalidProtocolBufferException;
import org.redisson.api.RBlockingQueue;
import tv.v1x1.common.dto.messages.Message;

/**
 * @author Cobi
 */
public class MessageQueueImpl implements MessageQueue {

    private final RBlockingQueue<byte[]> blockingQueue;

    public MessageQueueImpl(final RBlockingQueue<byte[]> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public Message get() throws InterruptedException, InvalidProtocolBufferException {
        return Message.fromBytes(blockingQueue.take());
    }

    @Override
    public void add(final Message message) {
        blockingQueue.add(message.toBytes());
    }
}
