package tv.v1x1.modules.channel.wasm;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.modules.channel.wasm.api.V1x1WebAssemblyModuleDef;
import tv.v1x1.modules.channel.wasm.config.ModuleUserConfiguration;
import tv.v1x1.modules.channel.wasm.config.WebAssemblyUserConfiguration;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.decoder.ModuleDef;
import tv.v1x1.modules.channel.wasm.vm.store.LinkingException;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;

public class ExecutionEnvironment {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int MAX_INSTRUCTIONS = 262144;
    private static final int TRAP_HOLD_TIME = 60000;

    public static class CacheKey {
        private final Tenant tenant;
        private final WebAssemblyUserConfiguration configuration;

        public CacheKey(final Tenant tenant, final WebAssemblyUserConfiguration configuration) {
            this.tenant = tenant;
            this.configuration = configuration;
        }

        public Tenant getTenant() {
            return tenant;
        }

        public WebAssemblyUserConfiguration getConfiguration() {
            return configuration;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final CacheKey cacheKey = (CacheKey) o;
            return Objects.equal(tenant, cacheKey.tenant) &&
                    Objects.equal(configuration, cacheKey.configuration);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(tenant, configuration);
        }
    }

    private final WebAssembly module;
    private final Tenant tenant;
    private final WebAssemblyUserConfiguration configuration;
    private WebAssemblyVirtualMachine virtualMachine;
    private Event currentEvent;
    private boolean trapped;
    private long lastTrapped;

    public ExecutionEnvironment(final WebAssembly module, final Tenant tenant, final WebAssemblyUserConfiguration configuration) {
        this.module = module;
        this.tenant = tenant;
        this.configuration = configuration;
        reset();
    }

    public static ExecutionEnvironment build(final WebAssembly module, final Tenant tenant, final WebAssemblyUserConfiguration configuration) {
        return new ExecutionEnvironment(module, tenant, configuration);
    }

    public void handleChatMessageEvent(final ChatMessageEvent chatMessageEvent) {
        if(isTrapped())
            return;
        this.currentEvent = chatMessageEvent;
        try {
            virtualMachine.callAllExports("event_handler", MAX_INSTRUCTIONS);
        } catch(final TrapException e) {
            handleTrapped();
        }
    }

    private void handleTrapped() {
        this.trapped = true;
        this.lastTrapped = System.currentTimeMillis();
    }

    private boolean isTrapped() {
        if(trapped && lastTrapped < System.currentTimeMillis() - TRAP_HOLD_TIME)
            reset();
        return trapped;
    }

    private void reset() {
        this.currentEvent = null;
        this.trapped = false;
        final WebAssemblyVirtualMachine virtualMachine = WebAssemblyVirtualMachine.build();
        final ModuleDef moduleDef = new V1x1WebAssemblyModuleDef(this);
        try {
            virtualMachine.loadModules(moduleDef);
            for(final Map.Entry<String, ModuleUserConfiguration> entry : configuration.getModules().entrySet())
                virtualMachine.loadModules(ModuleDef.fromString(entry.getKey(), entry.getValue().getData()));
        } catch (final ValidationException | LinkingException | TrapException | IOException e) {
            LOG.warn("Exception caught trying to load WebAssembly modules for {}", tenant, e);
        }
    }
}
