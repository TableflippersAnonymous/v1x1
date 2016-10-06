package tv.twitchbot.common.services.queue;

import com.google.protobuf.InvalidProtocolBufferException;
import tv.twitchbot.common.dto.messages.Message;

/**
 * Created by naomi on 10/5/16.
 */
public interface MessageQueue {
    Message get() throws InterruptedException, InvalidProtocolBufferException;
    void add(Message message);
}
