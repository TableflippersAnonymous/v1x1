package tv.v1x1.modules.channel.wasm.vm.instructions.numeric;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.types.I64;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class I64CvtOpInstruction<T extends WebAssemblyType> extends Instruction {
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
        final I64 val = virtualMachine.getStack().pop(I64.class);
        virtualMachine.getStack().push(op(val));
    }

    public abstract T op(final I64 val) throws TrapException;
}
