package tv.twitchbot.modules.core.tmi;

import tv.twitchbot.common.dto.messages.requests.SendMessageRequest;
import tv.twitchbot.common.dto.messages.responses.SendMessageResponse;
import tv.twitchbot.common.modules.GlobalConfiguration;
import tv.twitchbot.common.modules.Module;
import tv.twitchbot.common.modules.ModuleSettings;
import tv.twitchbot.common.modules.TenantConfiguration;
import tv.twitchbot.common.rpc.services.Service;

/**
 * Created by naomi on 10/8/2016.
 */
public class TmiService extends Service<SendMessageRequest, SendMessageResponse> {
    public TmiService(Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> module) {
        super(module, "Core|TMI", SendMessageRequest.class);
    }

    @Override
    protected SendMessageResponse call(SendMessageRequest request) {
        return null;
    }
}
