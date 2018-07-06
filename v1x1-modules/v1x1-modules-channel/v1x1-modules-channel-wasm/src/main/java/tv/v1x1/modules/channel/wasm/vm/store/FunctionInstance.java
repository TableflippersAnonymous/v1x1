package tv.v1x1.modules.channel.wasm.vm.store;

import tv.v1x1.modules.channel.wasm.vm.FunctionType;

public abstract class FunctionInstance {
    private final FunctionType type;

    public FunctionInstance(final FunctionType type) {
        this.type = type;
    }

    public FunctionType getType() {
        return type;
    }
}
