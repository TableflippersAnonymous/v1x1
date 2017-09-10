package tv.v1x1.modules.core.chatrouter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.messages.requests.SendMessageRequest;
import tv.v1x1.common.dto.messages.responses.SendMessageResponse;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.rpc.services.ThreadedService;
import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.channel.CreateMessageRequest;
import tv.v1x1.common.services.discord.dto.channel.Message;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 10/16/2016.
 */
public class ChatRouterService extends ThreadedService<SendMessageRequest, SendMessageResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LoadingCache<String, TmiServiceClient> tmiCache;
    private final DiscordApi discordApi;

    public ChatRouterService(final Module<?, ?> module) {
        super(module, "ChatRouter", SendMessageRequest.class);
        tmiCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .removalListener((RemovalListener<String, TmiServiceClient>) removalNotification -> removalNotification.getValue().shutdown())
                .build(new CacheLoader<String, TmiServiceClient>() {
                    @Override
                    public TmiServiceClient load(final String s) throws Exception {
                        return new TmiServiceClient(module, s);
                    }
                });
        discordApi = module.getInjector().getInstance(DiscordApi.class);
    }

    @Override
    protected SendMessageResponse call(final SendMessageRequest request) {
        try {
            LOG.info("Forwarding message={} to {}", request.getText(), request.getDestination().getDisplayName());
            switch(request.getDestination().getChannelGroup().getPlatform()) {
                case TWITCH:
                    tmiCache.get(request.getDestination().getId()).sendMessage(request.getDestination(), request.getText()).get(10, TimeUnit.SECONDS);
                    break;
                case DISCORD:
                    final Message message = discordApi.getChannels().createMessage(request.getDestination().getId(), new CreateMessageRequest(request.getText()));
                    LOG.info("Created message: {}", message.getId());
                    break;
                default:
                    LOG.warn("Unknown platform type: {}", request.getDestination().getChannelGroup().getPlatform().name());
            }
            return new SendMessageResponse(getModule().toDto(), request.getMessageId());
        } catch (final Exception e) {
            LOG.error("Got exception", e);
            throw new RuntimeException(e);
        }
    }
}
