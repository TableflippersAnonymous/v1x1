package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.unop;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.types.I64;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;

public class I64EqzInstruction extends Instruction {
    @Override
    public void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException {
        /* No action */
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        stack.popOperands(ValType.I64);
        stack.pushOperands(ValType.I32);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final I64 val = virtualMachine.getStack().pop(I64.class);
        virtualMachine.getStack().push(val.eqz());
    }
}
