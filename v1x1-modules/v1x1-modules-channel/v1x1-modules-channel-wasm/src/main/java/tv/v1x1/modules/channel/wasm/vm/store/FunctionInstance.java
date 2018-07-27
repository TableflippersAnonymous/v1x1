package tv.v1x1.modules.channel.wasm.vm.store;

import tv.v1x1.modules.channel.wasm.vm.runtime.ModuleInstance;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.runtime.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.validation.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;

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

    @Override
    public String toString() {
        return "FunctionInstance{" +
                "type=" + type +
                ", locals=" + locals +
                '}';
    }
}
