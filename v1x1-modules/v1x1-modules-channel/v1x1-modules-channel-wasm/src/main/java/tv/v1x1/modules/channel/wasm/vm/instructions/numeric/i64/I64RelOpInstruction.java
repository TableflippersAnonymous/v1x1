package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64;

import tv.v1x1.modules.channel.wasm.vm.runtime.Instruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.runtime.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.I64;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;
import tv.v1x1.modules.channel.wasm.vm.validation.WebAssemblyValidationStack;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class I64RelOpInstruction extends Instruction {
    @Override
    public void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException {
        /* No action */
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        stack.popOperands(ValType.I64, ValType.I64);
        stack.pushOperand(ValType.I32);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final I64 val2 = virtualMachine.getStack().pop(I64.class);
        final I64 val1 = virtualMachine.getStack().pop(I64.class);
        virtualMachine.getStack().push(op(val1, val2));
    }

    public abstract I32 op(final I64 val1, final I64 val2) throws TrapException;
}
