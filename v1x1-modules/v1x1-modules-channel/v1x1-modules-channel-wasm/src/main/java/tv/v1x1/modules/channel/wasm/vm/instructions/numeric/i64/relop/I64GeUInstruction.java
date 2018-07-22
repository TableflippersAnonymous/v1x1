package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.relop;

import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.I64RelOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.I64;

public class I64GeUInstruction extends I64RelOpInstruction {
    @Override
    public I32 op(final I64 val1, final I64 val2) throws TrapException {
        return val1.geU(val2);
    }
}
