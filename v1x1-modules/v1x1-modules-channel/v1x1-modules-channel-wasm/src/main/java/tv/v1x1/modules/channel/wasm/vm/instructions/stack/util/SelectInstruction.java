package tv.v1x1.modules.channel.wasm.vm.instructions.stack.util;

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

public class SelectInstruction extends Instruction {
    @Override
    public void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException {
        /* No action */
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        stack.popOperand(ValType.I32);
        final ValType type1 = stack.popOperand();
        final ValType type2 = stack.popOperand(type1);
        stack.pushOperand(type2);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final I32 condition = virtualMachine.getStack().pop(I32.class);
        final WebAssemblyType val2 = virtualMachine.getStack().pop(WebAssemblyType.class);
        final WebAssemblyType val1 = virtualMachine.getStack().pop(val2.getClass());
        virtualMachine.getStack().push(condition.eqz() == I32.ZERO ? val1 : val2);
    }
}
