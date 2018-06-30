package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.unop;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.io.DataInputStream;
import java.io.IOException;

public class I32EqzInstruction extends Instruction {
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
        final I32 val = virtualMachine.getStack().pop(I32.class);
        virtualMachine.getStack().push(val.eqz());
    }
}
