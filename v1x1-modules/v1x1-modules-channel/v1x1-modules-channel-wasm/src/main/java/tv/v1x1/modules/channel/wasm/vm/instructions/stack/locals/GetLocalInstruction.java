package tv.v1x1.modules.channel.wasm.vm.instructions.stack.locals;

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

public class GetLocalInstruction extends Instruction {
    private I32 idx;
    private ValType valType;

    @Override
    public void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException {
        idx = I32.decodeU(dataInputStream);
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        if(context.getLocals().size() <= idx.getValU())
            throw new ValidationException();
        valType = context.getLocals().get((int) idx.getValU());
        stack.pushOperand(valType);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final WebAssemblyType val = virtualMachine.getCurrentActivation().getLocal((int) idx.getValU(), valType.getTypeClass());
        virtualMachine.getStack().push(val);
    }
}
