package tv.v1x1.modules.channel.wasm.vm.instructions.stack;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;

public class TeeLocalInstruction extends Instruction {
    private I32 idx;
    private ValType valType;

    @Override
    public void decode(final DataInputStream dataInputStream) throws IOException {
        idx = I32.decodeU(dataInputStream);
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        if(context.getLocals().size() <= idx.getVal())
            throw new ValidationException();
        valType = context.getLocals().get(idx.getVal());
        stack.popOperand(valType);
        stack.pushOperand(valType);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final WebAssemblyType val = virtualMachine.getStack().pop(valType.getTypeClass());
        virtualMachine.getCurrentActivation().setLocal(idx.getVal(), val);
        virtualMachine.getStack().push(val);
    }
}
