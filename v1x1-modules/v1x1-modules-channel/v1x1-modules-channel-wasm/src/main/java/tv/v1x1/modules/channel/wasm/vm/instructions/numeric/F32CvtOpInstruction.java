package tv.v1x1.modules.channel.wasm.vm.instructions.numeric;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.types.F32;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class F32CvtOpInstruction<T extends WebAssemblyType> extends Instruction {
    @Override
    public void decode(final DataInputStream dataInputStream) throws IOException {
        /* No action */
    }

    @Override
    public boolean validate(final Context context) {
        return true;
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final F32 val = virtualMachine.getStack().pop(F32.class);
        virtualMachine.getStack().push(op(val));
    }

    public abstract T op(final F32 val) throws TrapException;
}
