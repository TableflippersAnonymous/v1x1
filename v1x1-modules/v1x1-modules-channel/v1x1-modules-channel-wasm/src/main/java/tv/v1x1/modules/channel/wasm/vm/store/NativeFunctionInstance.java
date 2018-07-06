package tv.v1x1.modules.channel.wasm.vm.store;

import tv.v1x1.modules.channel.wasm.vm.FunctionType;

public abstract class NativeFunctionInstance extends FunctionInstance {
    public NativeFunctionInstance(final FunctionType type) {
        super(type);
    }
}
