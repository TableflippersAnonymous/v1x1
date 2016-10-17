package tv.twitchbot.modules.channel.echo;

import tv.twitchbot.common.modules.RegisteredThreadedModule;
import tv.twitchbot.common.rpc.client.ChatRouterServiceClient;

/**
 * @author Josh
 */
public class Echo extends RegisteredThreadedModule<EchoModuleSettings, EchoGlobalConfiguration, EchoTenantConfiguration> {
    ChatRouterServiceClient crsc;

    @Override
    public String getName() {
        return "echo";
    }
    @Override
    public void initialize() {
        super.initialize();
        crsc = getServiceClient(ChatRouterServiceClient.class);
        registerListener(new EchoListener(this));
    }
}
