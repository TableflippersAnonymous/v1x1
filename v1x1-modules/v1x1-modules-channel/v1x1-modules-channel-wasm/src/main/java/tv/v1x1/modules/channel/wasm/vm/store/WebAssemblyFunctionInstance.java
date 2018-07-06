package tv.v1x1.modules.channel.wasm.vm.store;

import tv.v1x1.modules.channel.wasm.vm.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.ModuleInstance;

public class WebAssemblyFunctionInstance extends FunctionInstance {
    private final ModuleInstance module;
    private final Instruction start;

    public WebAssemblyFunctionInstance(final FunctionType type, final ModuleInstance module, final Instruction start) {
        super(type);
        this.module = module;
        this.start = start;
    }

    public ModuleInstance getModule() {
        return module;
    }

    public Instruction getStart() {
        return start;
    }
}
