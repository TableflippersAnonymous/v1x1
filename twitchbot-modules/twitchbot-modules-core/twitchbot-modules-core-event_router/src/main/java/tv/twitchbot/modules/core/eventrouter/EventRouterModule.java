package tv.twitchbot.modules.core.eventrouter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.messages.Message;
import tv.twitchbot.common.modules.ThreadedModule;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 10/16/2016.
 */
public class EventRouterModule extends ThreadedModule<EventRouterSettings, EventRouterGlobalConfiguration, EventRouterTenantConfiguration> {
    LoadingCache<String, Set<Module>> moduleCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Set<Module>>() {
                @Override
                public Set<Module> load(String s) throws Exception {
                    return getModuleRegistry().modules();
                }
            });

    @Override
    protected void processMessage(Message message) {
        if(!(message instanceof Event))
            return;

        try {
            Set<Module> modules = moduleCache.get("modules");
            for(Module module : modules)
                send(getMainQueueForModule(module), message);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return "event_router";
    }

    @Override
    protected void initialize() {

    }

    public static void main(String[] args) throws Exception {
        new EventRouterModule().entryPoint(args);
    }
}
