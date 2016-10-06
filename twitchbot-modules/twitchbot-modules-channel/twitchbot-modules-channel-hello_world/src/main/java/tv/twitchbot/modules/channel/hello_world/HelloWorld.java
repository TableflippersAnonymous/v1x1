package tv.twitchbot.modules.channel.hello_world;

import tv.twitchbot.common.dto.core.Channel;
import tv.twitchbot.common.dto.messages.events.ChatMessageEvent;
import tv.twitchbot.common.dto.messages.requests.SendMessageRequest;
import tv.twitchbot.common.modules.DefaultModule;

/**
 * Created by Josh on 2016-10-06.
 */
public class HelloWorld extends DefaultModule<HelloWorldSettings, HelloWorldGlobalConfiguration, HelloWorldTenantConfiguration> {

    @Override
    protected String getName() {
        return "hello_world";
    }

    public static void main(String[] args) throws Exception {
        new HelloWorld().entryPoint(args);
    }

    @Override
    protected void processChatMessageEvent(ChatMessageEvent chatMessageEvent) {
        super.processChatMessageEvent(chatMessageEvent);
        String queueName = super.getQueueName();
        Channel chan = chatMessageEvent.getChatMessage().getChannel();
        SendMessageRequest msg = new SendMessageRequest(toDto(), queueName, chan, "Hello, world!");
        super.send(queueName, msg);
    }
}
