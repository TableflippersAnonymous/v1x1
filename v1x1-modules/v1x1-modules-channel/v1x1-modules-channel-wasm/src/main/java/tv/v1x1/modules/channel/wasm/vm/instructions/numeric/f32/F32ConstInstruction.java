package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.types.F32;

import java.io.DataInputStream;
import java.io.IOException;

public class F32ConstInstruction extends Instruction {
    private F32 val;

    @Override
    public void decode(final DataInputStream dataInputStream) throws IOException {
        val = F32.decode(dataInputStream);
    }

    @Override
    public boolean validate(final Context context) {
        return true;
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        virtualMachine.getStack().push(val);
    }
}
