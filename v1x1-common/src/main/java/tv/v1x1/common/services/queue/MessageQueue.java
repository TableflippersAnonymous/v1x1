package tv.v1x1.common.services.queue;

import com.google.protobuf.InvalidProtocolBufferException;
import tv.v1x1.common.dto.messages.Message;

import java.util.concurrent.Callable;
/**
 * @author Cobi
 */
public interface MessageQueue {
    /**
     * Removes a message from the front of the message queue
     * @return The first message
     * @throws InterruptedException
     * @throws InvalidProtocolBufferException
     */
    Message get() throws Exception;

    Message getWithOthers(final Callable<String[]> otherQueueNames) throws Exception;

    /**
     * Add a message to the end of the message queue
     * @param message
     */
    void add(Message message);
}
