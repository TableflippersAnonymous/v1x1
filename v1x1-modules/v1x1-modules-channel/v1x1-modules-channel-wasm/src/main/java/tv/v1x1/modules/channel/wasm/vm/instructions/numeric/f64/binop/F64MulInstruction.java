package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.binop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.F64BinOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F64;

public class F64MulInstruction extends F64BinOpInstruction {
    @Override
    public F64 op(final F64 val1, final F64 val2) throws TrapException {
        return val1.mul(val2);
    }
}
