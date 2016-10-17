package tv.twitchbot.modules.core.chatrouter;

import com.google.common.cache.*;
import tv.twitchbot.common.dto.messages.requests.SendMessageRequest;
import tv.twitchbot.common.dto.messages.responses.SendMessageResponse;
import tv.twitchbot.common.modules.GlobalConfiguration;
import tv.twitchbot.common.modules.Module;
import tv.twitchbot.common.modules.ModuleSettings;
import tv.twitchbot.common.modules.TenantConfiguration;
import tv.twitchbot.common.rpc.services.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 10/16/2016.
 */
public class ChatRouterService extends Service<SendMessageRequest, SendMessageResponse> {
    private LoadingCache<String, TmiServiceClient> tmiCache;

    public ChatRouterService(Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> module) {
        super(module, "ChatRouter", SendMessageRequest.class);
        tmiCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .removalListener(new RemovalListener<String, TmiServiceClient>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, TmiServiceClient> removalNotification) {
                        removalNotification.getValue().shutdown();
                    }
                })
                .build(new CacheLoader<String, TmiServiceClient>() {
                    @Override
                    public TmiServiceClient load(String s) throws Exception {
                        return new TmiServiceClient(module, s);
                    }
                });
    }

    @Override
    protected SendMessageResponse call(SendMessageRequest request) {
        try {
            SendMessageResponse response = tmiCache.get(request.getDestination().getId()).sendMessage(request.getDestination(), request.getText()).get();
            return new SendMessageResponse(getModule().toDto(), request.getMessageId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
