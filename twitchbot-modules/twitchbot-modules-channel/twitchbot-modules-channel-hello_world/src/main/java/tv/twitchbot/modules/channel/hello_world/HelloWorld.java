package tv.twitchbot.modules.channel.hello_world;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.messages.Message;
import tv.twitchbot.common.dto.messages.events.ChatMessageEvent;
import tv.twitchbot.common.modules.DefaultModule;
import tv.twitchbot.common.util.commands.CommandDelegator;

/**
 * Created by Josh on 2016-10-06.
 */
public class HelloWorld extends DefaultModule<HelloWorldSettings, HelloWorldGlobalConfiguration, HelloWorldTenantConfiguration> {
    CommandDelegator delegator;
    public static void main(String[] args) throws Exception {
        new HelloWorld().entryPoint(args);
    }

    @Override
    protected String getName() {
        return "hello_world";
    }

    @Override
    protected void initialize() {
        super.initialize();
        delegator = new CommandDelegator("!");
        delegator.registerCommand(new HelloWorldCommand(this));
    }

    @Override
    protected void processChatMessageEvent(ChatMessageEvent chatMessageEvent) {
        super.processChatMessageEvent(chatMessageEvent);
        delegator.handleChatMessage(chatMessageEvent);
    }

    /* Overriden so external Command can use required features */
    @Override
    protected String getQueueName() {
        return super.getQueueName();
    }

    @Override
    protected void send(String queueName, Message message) {
        super.send(queueName, message);
    }

    @Override
    protected Module toDto() {
        return super.toDto();
    }
}
