package tv.twitchbot.common.services.queue;

import tv.twitchbot.common.dto.messages.Message;

/**
 * Created by cobi on 10/5/16.
 */
public interface MessageQueue {
    Message get();
    void add(Message message);
}
