package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.cvtop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.I64CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F64;
import tv.v1x1.modules.channel.wasm.vm.types.I64;

public class F64ConvertSI64Instruction extends I64CvtOpInstruction<F64> {
    @Override
    public F64 op(final I64 val) throws TrapException {
        return val.convertSF64();
    }
}
