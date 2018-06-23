package tv.v1x1.modules.channel.wasm;

import tv.v1x1.common.dto.messages.Message;
import tv.v1x1.common.modules.ThreadedModule;
import tv.v1x1.modules.channel.wasm.config.WebAssemblyGlobalConfiguration;
import tv.v1x1.modules.channel.wasm.config.WebAssemblyUserConfiguration;

public class WebAssembly extends ThreadedModule<WebAssemblyGlobalConfiguration, WebAssemblyUserConfiguration> {
    @Override
    protected void processMessage(final Message message) {

    }

    @Override
    public String getName() {
        return "wasm";
    }

    @Override
    protected void initialize() {

    }
}
