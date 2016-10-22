package tv.v1x1.common.services.queue;

import com.google.protobuf.InvalidProtocolBufferException;
import tv.v1x1.common.dto.messages.Message;

/**
 * @author Naomi
 */
public interface MessageQueue {
    /**
     * Removes a message from the front of the message queue
     * @return The first message
     * @throws InterruptedException
     * @throws InvalidProtocolBufferException
     */
    Message get() throws InterruptedException, InvalidProtocolBufferException;

    /**
     * Add a message to the end of the message queue
     * @param message
     */
    void add(Message message);
}
