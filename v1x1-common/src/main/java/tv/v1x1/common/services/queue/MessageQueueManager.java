package tv.v1x1.common.services.queue;

/**
 * Created by cobi on 10/5/16.
 */
public interface MessageQueueManager {
    MessageQueue forName(String name);
}
