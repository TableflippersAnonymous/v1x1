package tv.v1x1.modules.channel.echo;

import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.rpc.client.ChatRouterServiceClient;

/**
 * @author Josh
 */
public class Echo extends RegisteredThreadedModule<EchoModuleSettings, EchoGlobalConfiguration, EchoTenantConfiguration> {
    ChatRouterServiceClient crsc;

    public static void main(final String[] args) throws Exception {
        new Echo().entryPoint(args);
    }
    
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
