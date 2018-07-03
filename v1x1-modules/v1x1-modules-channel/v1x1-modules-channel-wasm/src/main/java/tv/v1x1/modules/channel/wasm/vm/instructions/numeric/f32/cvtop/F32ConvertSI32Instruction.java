package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.cvtop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.I32CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F32;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

public class F32ConvertSI32Instruction extends I32CvtOpInstruction<F32> {
    @Override
    public F32 op(final I32 val) throws TrapException {
        return val.convertSF32();
    }
}
