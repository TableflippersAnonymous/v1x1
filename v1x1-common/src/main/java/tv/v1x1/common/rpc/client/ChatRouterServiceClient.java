package tv.v1x1.common.rpc.client;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.messages.requests.SendMessageRequest;
import tv.v1x1.common.dto.messages.responses.SendMessageResponse;
import tv.v1x1.common.modules.GlobalConfiguration;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.modules.ModuleSettings;
import tv.v1x1.common.modules.TenantConfiguration;

import java.util.concurrent.Future;

/**
 * Interfaces with chat modules to send messages
 * @author Naomi
 */
public class ChatRouterServiceClient extends ServiceClient<SendMessageRequest, SendMessageResponse> {

    public ChatRouterServiceClient(final Module<?, ?, ?, ?> module) {
        super(module, SendMessageResponse.class);
    }

    public Future<SendMessageResponse> sendMessage(final Channel channel, final String text) {
        return send(new SendMessageRequest(getModule(), getQueueName(), channel, text));
    }

    @Override
    protected String getServiceName() {
        return "ChatRouter";
    }
}
