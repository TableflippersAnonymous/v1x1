package tv.v1x1.modules.channel.wasm.vm.instructions;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;

import java.io.DataInputStream;

public class UnreachableInstruction extends Instruction {
    @Override
    public void decode(final DataInputStream dataInputStream) {
        /* No action */
    }

    @Override
    public boolean validate(final Context context) {
        return true;
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        throw new TrapException();
    }
}
