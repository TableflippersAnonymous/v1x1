package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.cvtop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.F32CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F32;
import tv.v1x1.modules.channel.wasm.vm.types.I64;

public class I64TruncSF32Instruction extends F32CvtOpInstruction<I64> {
    @Override
    public I64 op(final F32 val) throws TrapException {
        return val.truncSI64();
    }
}
