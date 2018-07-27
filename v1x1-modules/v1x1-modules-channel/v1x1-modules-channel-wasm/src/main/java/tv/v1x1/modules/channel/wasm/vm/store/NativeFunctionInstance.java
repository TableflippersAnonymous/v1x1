package tv.v1x1.modules.channel.wasm.vm.store;

import tv.v1x1.modules.channel.wasm.vm.runtime.ModuleInstance;
import tv.v1x1.modules.channel.wasm.vm.validation.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;

import java.util.List;

public abstract class NativeFunctionInstance extends FunctionInstance {
    public NativeFunctionInstance(final FunctionType type, final List<ValType> locals, final ModuleInstance module) {
        super(type, locals, module);
    }
}
