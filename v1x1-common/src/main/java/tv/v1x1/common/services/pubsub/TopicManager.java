package tv.v1x1.common.services.pubsub;

import tv.v1x1.common.dto.messages.PubSubMessage;

/**
 * Created by naomi on 4/30/2017.
 */
public interface TopicManager {
    void publish(String topic, PubSubMessage pubSubMessage);
    int addListener(String topic, TopicListener topicListener);
    void removeListener(String topic, int listenerId);
}
