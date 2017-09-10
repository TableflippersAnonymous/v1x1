package tv.v1x1.modules.core.chatrouter;

import tv.v1x1.common.modules.ServiceModule;

/**
 * Created by naomi on 10/16/2016.
 */
public class ChatRouterModule extends ServiceModule<ChatRouterGlobalConfiguration, ChatRouterUserConfiguration> {
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
