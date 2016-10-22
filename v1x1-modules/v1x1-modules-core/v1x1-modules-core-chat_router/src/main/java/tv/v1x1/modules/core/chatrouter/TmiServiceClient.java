package tv.v1x1.modules.core.chatrouter;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.messages.requests.SendMessageRequest;
import tv.v1x1.common.dto.messages.responses.SendMessageResponse;
import tv.v1x1.common.modules.GlobalConfiguration;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.modules.ModuleSettings;
import tv.v1x1.common.modules.TenantConfiguration;
import tv.v1x1.common.rpc.client.ServiceClient;

import java.util.concurrent.Future;

/**
 * Created by cobi on 10/16/2016.
 */
public class TmiServiceClient extends ServiceClient<SendMessageRequest, SendMessageResponse> {
    private final String channel;

    public TmiServiceClient(final Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> module, final String channel) {
        super(module, SendMessageResponse.class);
        this.channel = channel;
    }

    @Override
    protected String getServiceName() {
        return "Core|TMI|Channel|" + channel;
    }

    public Future<SendMessageResponse> sendMessage(final Channel channel, final String text) {
        return send(new SendMessageRequest(getModule(), getQueueName(), channel, text));
    }
}
