package tv.v1x1.modules.channel.wasm.vm.store;

import tv.v1x1.modules.channel.wasm.vm.runtime.Instruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.ModuleInstance;
import tv.v1x1.modules.channel.wasm.vm.runtime.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.validation.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;

import java.util.List;

public class WebAssemblyFunctionInstance extends FunctionInstance {
    private final Instruction start;

    public WebAssemblyFunctionInstance(final FunctionType type, final List<ValType> locals, final ModuleInstance module, final Instruction start) {
        super(type, locals, module);
        this.start = start;
    }

    public Instruction getStart() {
        return start;
    }

    @Override
    public void invoke(final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance previousModule) {
        virtualMachine.setNextInstruction(start);
    }
}
