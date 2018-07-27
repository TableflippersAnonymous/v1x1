package tv.v1x1.modules.channel.wasm.vm.instructions.store.globals;

import tv.v1x1.modules.channel.wasm.vm.runtime.Instruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.runtime.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;
import tv.v1x1.modules.channel.wasm.vm.validation.WebAssemblyValidationStack;

import java.io.DataInputStream;
import java.io.IOException;

public class GetGlobalInstruction extends Instruction {
    private I32 idx;

    @Override
    public void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException {
        idx = I32.decodeU(dataInputStream);
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        if(context.getGlobals().size() <= idx.getValU())
            throw new ValidationException();
        final ValType valType = context.getGlobals().get((int) idx.getValU()).getValType();
        stack.pushOperand(valType);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final int globalIdx = virtualMachine.getCurrentActivation().getModule().getGlobalAddresses()[(int) idx.getValU()];
        final WebAssemblyType val = virtualMachine.getStore().getGlobals().get(globalIdx).getValue();
        virtualMachine.getStack().push(val);
    }

    @Override
    protected boolean isConstant() {
        return true;
    }
}
