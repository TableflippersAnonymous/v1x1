package tv.v1x1.modules.channel.wasm.vm.store;

import tv.v1x1.modules.channel.wasm.vm.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.ModuleInstance;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;

import java.util.List;

public abstract class FunctionInstance {
    private final FunctionType type;
    private final List<ValType> locals;
    private final ModuleInstance module;

    public FunctionInstance(final FunctionType type, final List<ValType> locals, final ModuleInstance module) {
        this.type = type;
        this.locals = locals;
        this.module = module;
    }

    public ModuleInstance getModule() {
        return module;
    }

    public FunctionType getType() {
        return type;
    }

    public List<ValType> getLocals() {
        return locals;
    }

    public abstract void invoke(final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance previousModule) throws TrapException;
}
