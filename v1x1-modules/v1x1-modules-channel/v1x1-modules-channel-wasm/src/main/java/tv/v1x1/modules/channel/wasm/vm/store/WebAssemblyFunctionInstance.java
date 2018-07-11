package tv.v1x1.modules.channel.wasm.vm.store;

import tv.v1x1.modules.channel.wasm.vm.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.ModuleInstance;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;

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
