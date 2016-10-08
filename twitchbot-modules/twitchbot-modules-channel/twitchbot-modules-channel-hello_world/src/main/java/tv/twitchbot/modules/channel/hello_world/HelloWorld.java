package tv.twitchbot.modules.channel.hello_world;

import tv.twitchbot.common.dto.messages.events.ChatMessageEvent;
import tv.twitchbot.common.modules.DefaultModule;
import tv.twitchbot.common.rpc.client.ChatRouterServiceClient;
import tv.twitchbot.common.util.commands.CommandDelegator;

/**
 * Created by Josh on 2016-10-06.
 */
public class HelloWorld extends DefaultModule<HelloWorldSettings, HelloWorldGlobalConfiguration, HelloWorldTenantConfiguration> {
    CommandDelegator delegator;
    ChatRouterServiceClient crsc;
    public static void main(String[] args) throws Exception {
        new HelloWorld().entryPoint(args);
    }

    @Override
    public String getName() {
        return "hello_world";
    }

    @Override
    protected void initialize() {
        super.initialize();
        delegator = new CommandDelegator("!");
        delegator.registerCommand(new HelloWorldCommand(this));
        crsc = getServiceClient(ChatRouterServiceClient.class);
    }

    @Override
    protected void processChatMessageEvent(ChatMessageEvent chatMessageEvent) {
        super.processChatMessageEvent(chatMessageEvent);
        delegator.handleChatMessage(chatMessageEvent);
    }
}
