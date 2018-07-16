package tv.v1x1.modules.channel.wasm;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.dto.messages.events.ConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.DiscordVoiceStateEvent;
import tv.v1x1.common.dto.messages.events.SchedulerNotifyEvent;
import tv.v1x1.common.modules.DefaultModule;
import tv.v1x1.modules.channel.wasm.config.WebAssemblyGlobalConfiguration;
import tv.v1x1.modules.channel.wasm.config.WebAssemblyUserConfiguration;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

public class WebAssembly extends DefaultModule<WebAssemblyGlobalConfiguration, WebAssemblyUserConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final LoadingCache<ExecutionEnvironment.CacheKey, ExecutionEnvironment> environments = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .maximumSize(50)
            .build(new CacheLoader<ExecutionEnvironment.CacheKey, ExecutionEnvironment>() {
                @Override
                public ExecutionEnvironment load(final ExecutionEnvironment.CacheKey cacheKey) throws Exception {
                    return ExecutionEnvironment.build(WebAssembly.this, cacheKey.getTenant(), cacheKey.getConfiguration());
                }
            });

    public static void main(final String[] args) {
        new WebAssembly().entryPoint(args);
    }

    @Override
    protected void processSchedulerNotifyEvent(final SchedulerNotifyEvent event) {

    }

    @Override
    protected void processChatMessageEvent(final ChatMessageEvent chatMessageEvent) {
        final WebAssemblyUserConfiguration configuration = getConfiguration(chatMessageEvent.getChatMessage().getChannel());
        if(!configuration.isEnabled())
            return;
        try {
            final Tenant tenant = chatMessageEvent.getChatMessage().getChannel().getChannelGroup().getTenant();
            final ExecutionEnvironment executionEnvironment = environments.get(new ExecutionEnvironment.CacheKey(tenant, configuration));
            executionEnvironment.handleChatMessageEvent(chatMessageEvent);
        } catch (final Exception e) {
            LOG.warn("Got exception processing message {}", chatMessageEvent, e);
        }
    }

    @Override
    protected void processConfigChangeEvent(final ConfigChangeEvent event) {

    }

    @Override
    protected void processDiscordVoiceStateEvent(final DiscordVoiceStateEvent event) {

    }

    @Override
    public String getName() {
        return "wasm";
    }

    @Override
    protected void initialize() {

    }
}
