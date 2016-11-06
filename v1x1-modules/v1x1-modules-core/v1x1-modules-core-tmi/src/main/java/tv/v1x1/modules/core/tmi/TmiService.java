package tv.v1x1.modules.core.tmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.messages.requests.SendMessageRequest;
import tv.v1x1.common.dto.messages.responses.SendMessageResponse;
import tv.v1x1.common.modules.GlobalConfiguration;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.modules.ModuleSettings;
import tv.v1x1.common.modules.TenantConfiguration;
import tv.v1x1.common.rpc.services.Service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * Created by naomi on 10/8/2016.
 */
public class TmiService extends Service<SendMessageRequest, SendMessageResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TmiBot bot;

    public TmiService(final Module<?, ?, ?, ?> module, final String channel, final TmiBot bot) {
        super(module, "Core|TMI|Channel|" + channel, SendMessageRequest.class);
        this.bot = bot;
    }

    @Override
    protected SendMessageResponse call(final SendMessageRequest request) {
        try {
            LOG.info("SendMessageRequest: {}: {}", request.getDestination().getId(), request.getText());
            bot.sendMessage(request.getDestination().getId(), request.getText());
            return new SendMessageResponse(getModule().toDto(), request.getMessageId());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
