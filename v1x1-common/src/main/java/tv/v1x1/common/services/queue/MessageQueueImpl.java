package tv.v1x1.common.services.queue;

import com.google.protobuf.InvalidProtocolBufferException;
import org.redisson.api.RBlockingQueue;
import tv.v1x1.common.dto.messages.Message;

import java.util.concurrent.TimeUnit;

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
        while (!Thread.interrupted()) {
            final byte[] bytes = blockingQueue.poll(50, TimeUnit.MILLISECONDS);
            if (bytes == null)
                continue;
            return Message.fromBytes(bytes);
        }
        throw new InterruptedException();
    }

    @Override
    public void add(final Message message) {
        blockingQueue.add(message.toBytes());
    }
}
