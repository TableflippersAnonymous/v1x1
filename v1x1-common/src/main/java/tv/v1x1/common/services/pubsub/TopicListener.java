package tv.v1x1.common.services.pubsub;

import tv.v1x1.common.dto.messages.PubSubMessage;

import java.util.EventListener;

/**
 * Created by cobi on 4/30/2017.
 */
public interface TopicListener extends EventListener {
    void onMessage(PubSubMessage pubSubMessage);
}
