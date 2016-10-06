package tv.twitchbot.common.modules;

import tv.twitchbot.common.dto.messages.Message;
import tv.twitchbot.common.services.queue.MessageQueue;
import tv.twitchbot.common.services.queue.MessageQueueManager;

/**
 * Created by naomi on 10/4/16.
 */
public abstract class Module {
    protected MessageQueueManager getMessageQueueManager() {
        return null;
    }

    protected GlobalConfiguration getGlobalConfiguration() {
        return null;
    }

    protected ChannelConfiguration getChannelConfiguration(String channel) {
        return null;
    }

    protected abstract String getName();

    protected abstract void handle(Message message);

    public void run() {
        MessageQueueManager mqm = getMessageQueueManager();
        MessageQueue mq = mqm.forName("inbound|module|" + getName());
        for(;;) {
            Message message = mq.get();
            handle(message);
        }
    }

    protected tv.twitchbot.common.dto.core.Module toDto() {
        return new tv.twitchbot.common.dto.core.Module(getName());
    }
}
