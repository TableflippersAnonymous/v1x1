package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.binop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.F32BinOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F32;

public class F32CopysignInstruction extends F32BinOpInstruction {
    @Override
    public F32 op(final F32 val1, final F32 val2) throws TrapException {
        return val1.copysign(val2);
    }
}
