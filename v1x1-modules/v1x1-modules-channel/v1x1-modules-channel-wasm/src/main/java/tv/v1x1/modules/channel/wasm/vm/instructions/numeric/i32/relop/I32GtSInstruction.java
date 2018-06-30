package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.relop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.I32RelOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

public class I32GtSInstruction extends I32RelOpInstruction {
    @Override
    public I32 op(final I32 val1, final I32 val2) throws TrapException {
        return val1.gtS(val2);
    }
}
