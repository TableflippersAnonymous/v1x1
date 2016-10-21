package tv.twitchbot.modules.core.chatrouter;

import tv.twitchbot.common.modules.ServiceModule;

/**
 * Created by cobi on 10/16/2016.
 */
public class ChatRouterModule extends ServiceModule<ChatRouterSettings, ChatRouterGlobalConfiguration, ChatRouterTenantConfiguration> {
    @Override
    public String getName() {
        return "chat_router";
    }

    @Override
    protected void initialize() {
        super.initialize();
        registerService(new ChatRouterService(this));
    }

    public static void main(final String[] args) throws Exception {
        new ChatRouterModule().entryPoint(args);
    }
}
