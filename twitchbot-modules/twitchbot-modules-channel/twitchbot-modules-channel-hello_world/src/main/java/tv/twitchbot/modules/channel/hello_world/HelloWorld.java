package tv.twitchbot.modules.channel.hello_world;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.twitchbot.common.dto.messages.events.ChatMessageEvent;
import tv.twitchbot.common.modules.DefaultModule;
import tv.twitchbot.common.rpc.client.ChatRouterServiceClient;
import tv.twitchbot.common.util.commands.CommandDelegator;

import java.lang.invoke.MethodHandles;

/**
 * Created by Josh on 2016-10-06.
 */
public class HelloWorld extends DefaultModule<HelloWorldSettings, HelloWorldGlobalConfiguration, HelloWorldTenantConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
        LOG.debug("Got chat message: {}", chatMessageEvent.getChatMessage().getText());
        delegator.handleChatMessage(chatMessageEvent);
    }
}
