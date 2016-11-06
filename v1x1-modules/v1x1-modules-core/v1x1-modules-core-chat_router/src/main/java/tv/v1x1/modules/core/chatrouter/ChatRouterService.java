package tv.v1x1.modules.core.chatrouter;

import com.google.common.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.messages.requests.SendMessageRequest;
import tv.v1x1.common.dto.messages.responses.SendMessageResponse;
import tv.v1x1.common.modules.GlobalConfiguration;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.modules.ModuleSettings;
import tv.v1x1.common.modules.TenantConfiguration;
import tv.v1x1.common.rpc.services.ThreadedService;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by naomi on 10/16/2016.
 */
public class ChatRouterService extends ThreadedService<SendMessageRequest, SendMessageResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LoadingCache<String, TmiServiceClient> tmiCache;

    public ChatRouterService(final Module<?, ?, ?, ?> module) {
        super(module, "ChatRouter", SendMessageRequest.class);
        tmiCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .removalListener(new RemovalListener<String, TmiServiceClient>() {
                    @Override
                    public void onRemoval(final RemovalNotification<String, TmiServiceClient> removalNotification) {
                        removalNotification.getValue().shutdown();
                    }
                })
                .build(new CacheLoader<String, TmiServiceClient>() {
                    @Override
                    public TmiServiceClient load(final String s) throws Exception {
                        return new TmiServiceClient(module, s);
                    }
                });
    }

    @Override
    protected SendMessageResponse call(final SendMessageRequest request) {
        try {
            LOG.info("Forwarding message={} to {}", request.getText(), request.getDestination().getDisplayName());
            final SendMessageResponse response = tmiCache.get(request.getDestination().getId()).sendMessage(request.getDestination(), request.getText()).get(10, TimeUnit.SECONDS);
            return new SendMessageResponse(getModule().toDto(), request.getMessageId());
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
