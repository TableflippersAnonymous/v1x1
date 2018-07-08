package tv.v1x1.modules.channel.wasm.vm.instructions.store.globals;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.Mutable;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;

public class SetGlobalInstruction extends Instruction {
    private I32 idx;
    private ValType valType;

    @Override
    public void decode(final DataInputStream dataInputStream) throws IOException {
        idx = I32.decodeU(dataInputStream);
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        if(context.getGlobals().size() <= idx.getValU())
            throw new ValidationException();
        if(context.getGlobals().get((int) idx.getValU()).getMutable() != Mutable.VARIABLE)
            throw new ValidationException();
        valType = context.getGlobals().get((int) idx.getValU()).getValType();
        stack.popOperand(valType);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final int globalAddress = virtualMachine.getCurrentActivation().getModule().getGlobalAddresses()[(int) idx.getValU()];
        final WebAssemblyType val = virtualMachine.getStack().pop(valType.getTypeClass());
        virtualMachine.getStore().getGlobals().get(globalAddress).setValue(val);
    }
}
