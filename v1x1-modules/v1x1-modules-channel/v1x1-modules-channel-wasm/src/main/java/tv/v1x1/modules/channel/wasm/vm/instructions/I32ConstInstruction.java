package tv.v1x1.modules.channel.wasm.vm.instructions;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TrapException;

import java.io.DataInputStream;

public class I32ConstInstruction extends Instruction {
    @Override
    public void decode(final DataInputStream dataInputStream) {
        I32
    }

    @Override
    public boolean validate(final Context context) {
        return false;
    }

    @Override
    public void execute(final Context context) throws TrapException {

    }
}
