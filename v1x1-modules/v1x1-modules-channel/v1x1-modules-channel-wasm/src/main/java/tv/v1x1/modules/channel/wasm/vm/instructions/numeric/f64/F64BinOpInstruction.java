package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64;

import tv.v1x1.modules.channel.wasm.vm.runtime.Instruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.runtime.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.types.F64;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;
import tv.v1x1.modules.channel.wasm.vm.validation.WebAssemblyValidationStack;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class F64BinOpInstruction extends Instruction {
    @Override
    public void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException {
        /* No action */
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        stack.popOperands(ValType.F64, ValType.F64);
        stack.pushOperand(ValType.F64);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final F64 val2 = virtualMachine.getStack().pop(F64.class);
        final F64 val1 = virtualMachine.getStack().pop(F64.class);
        virtualMachine.getStack().push(op(val1, val2));
    }

    public abstract F64 op(final F64 val1, final F64 val2) throws TrapException;
}
