package tv.v1x1.modules.channel.wasm.vm.instructions;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TrapException;

public class UnreachableInstruction extends Instruction {
    @Override
    public boolean validate(final Context context) {
        return true;
    }

    @Override
    public void execute(final Context context) throws TrapException {
        throw new TrapException();
    }
}
