package tv.twitchbot.modules.core.tmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.twitchbot.common.dto.messages.requests.SendMessageRequest;
import tv.twitchbot.common.dto.messages.responses.SendMessageResponse;
import tv.twitchbot.common.modules.GlobalConfiguration;
import tv.twitchbot.common.modules.Module;
import tv.twitchbot.common.modules.ModuleSettings;
import tv.twitchbot.common.modules.TenantConfiguration;
import tv.twitchbot.common.rpc.services.Service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * Created by cobi on 10/8/2016.
 */
public class TmiService extends Service<SendMessageRequest, SendMessageResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TmiBot bot;

    public TmiService(Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> module, String channel, TmiBot bot) {
        super(module, "Core|TMI|Channel|" + channel, SendMessageRequest.class);
        this.bot = bot;
    }

    @Override
    protected SendMessageResponse call(SendMessageRequest request) {
        try {
            LOG.info("SendMessageRequest: {}: {}", request.getDestination().getId(), request.getText());
            bot.sendMessage(request.getDestination().getId(), request.getText());
            return new SendMessageResponse(getModule().toDto(), request.getMessageId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
