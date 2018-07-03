package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.cvtop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.I32CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F64;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

public class F64ConvertSI32Instruction extends I32CvtOpInstruction<F64> {
    @Override
    public F64 op(final I32 val) throws TrapException {
        return val.convertSF64();
    }
}
