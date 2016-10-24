package tv.v1x1.modules.core.eventrouter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.messages.Message;
import tv.v1x1.common.modules.ThreadedModule;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/16/2016.
 */
public class EventRouterModule extends ThreadedModule<EventRouterSettings, EventRouterGlobalConfiguration, EventRouterTenantConfiguration> {
    LoadingCache<String, Set<Module>> moduleCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Set<Module>>() {
                @Override
                public Set<Module> load(final String s) throws Exception {
                    return getModuleRegistry().liveModules();
                }
            });

    @Override
    protected void processMessage(final Message message) {
        if(!(message instanceof Event))
            return;

        try {
            final Set<Module> modules = moduleCache.get("modules");
            for(final Module module : modules)
                send(getMainQueueForModule(module), message);
        } catch (final ExecutionException e) {
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

    public static void main(final String[] args) throws Exception {
        new EventRouterModule().entryPoint(args);
    }
}
