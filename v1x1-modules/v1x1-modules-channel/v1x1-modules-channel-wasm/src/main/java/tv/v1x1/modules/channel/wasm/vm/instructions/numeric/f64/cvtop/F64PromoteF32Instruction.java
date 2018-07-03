package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.cvtop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.F32CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F32;
import tv.v1x1.modules.channel.wasm.vm.types.F64;

public class F64PromoteF32Instruction extends F32CvtOpInstruction<F64> {
    @Override
    public F64 op(final F32 val) throws TrapException {
        return val.promote();
    }
}
