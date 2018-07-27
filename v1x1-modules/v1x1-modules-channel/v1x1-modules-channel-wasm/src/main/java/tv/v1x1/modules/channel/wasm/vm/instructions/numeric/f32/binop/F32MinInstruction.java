package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.binop;

import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.F32BinOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.types.F32;

public class F32MinInstruction extends F32BinOpInstruction {
    @Override
    public F32 op(final F32 val1, final F32 val2) throws TrapException {
        return val1.min(val2);
    }
}
