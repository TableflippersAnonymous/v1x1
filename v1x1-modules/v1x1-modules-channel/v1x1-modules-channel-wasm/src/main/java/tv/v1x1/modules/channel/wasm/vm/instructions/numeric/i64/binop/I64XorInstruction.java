package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.binop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.I64BinOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I64;

public class I64XorInstruction extends I64BinOpInstruction {
    @Override
    public I64 op(final I64 val1, final I64 val2) throws TrapException {
        return val1.xor(val2);
    }
}
