package tv.twitchbot.common.rpc.client;

import tv.twitchbot.common.dto.core.Channel;
import tv.twitchbot.common.dto.messages.requests.SendMessageRequest;
import tv.twitchbot.common.dto.messages.responses.SendMessageResponse;
import tv.twitchbot.common.modules.GlobalConfiguration;
import tv.twitchbot.common.modules.Module;
import tv.twitchbot.common.modules.ModuleSettings;
import tv.twitchbot.common.modules.TenantConfiguration;

import java.util.concurrent.Future;

/**
 * Created by cobi on 10/8/2016.
 */
public class ChatRouterServiceClient extends ServiceClient<SendMessageRequest, SendMessageResponse> {

    public ChatRouterServiceClient(Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> module) {
        super(module, SendMessageResponse.class);
    }

    public Future<SendMessageResponse> sendMessage(Channel channel, String text) {
        return send(new SendMessageRequest(getModule(), getQueueName(), channel, text));
    }

    @Override
    protected String getServiceName() {
        return "ChatRouter";
    }
}
