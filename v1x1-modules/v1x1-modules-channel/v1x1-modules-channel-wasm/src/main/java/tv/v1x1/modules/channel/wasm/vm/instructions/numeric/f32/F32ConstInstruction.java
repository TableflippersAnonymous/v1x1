package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32;

import tv.v1x1.modules.channel.wasm.vm.runtime.Instruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.runtime.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.types.F32;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;
import tv.v1x1.modules.channel.wasm.vm.validation.WebAssemblyValidationStack;

import java.io.DataInputStream;
import java.io.IOException;

public class F32ConstInstruction extends Instruction {
    private F32 val;

    @Override
    public void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException {
        val = F32.decode(dataInputStream);
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        stack.pushOperand(ValType.F32);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        virtualMachine.getStack().push(val);
    }

    @Override
    protected boolean isConstant() {
        return true;
    }
}
