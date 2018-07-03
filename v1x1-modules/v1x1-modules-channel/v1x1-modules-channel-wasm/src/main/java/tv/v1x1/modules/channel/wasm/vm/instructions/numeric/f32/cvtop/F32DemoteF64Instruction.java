package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.cvtop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.F64CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F32;
import tv.v1x1.modules.channel.wasm.vm.types.F64;

public class F32DemoteF64Instruction extends F64CvtOpInstruction<F32> {
    @Override
    public F32 op(final F64 val) throws TrapException {
        return val.demote();
    }
}
