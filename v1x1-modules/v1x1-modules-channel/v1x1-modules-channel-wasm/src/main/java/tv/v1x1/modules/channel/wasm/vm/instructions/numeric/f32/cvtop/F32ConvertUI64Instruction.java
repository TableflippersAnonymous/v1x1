package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.cvtop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.I64CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F32;
import tv.v1x1.modules.channel.wasm.vm.types.I64;

public class F32ConvertUI64Instruction extends I64CvtOpInstruction<F32> {
    @Override
    public F32 op(final I64 val) throws TrapException {
        return val.convertUF32();
    }
}
