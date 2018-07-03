package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.cvtop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.F64CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F64;
import tv.v1x1.modules.channel.wasm.vm.types.I64;

public class I64TruncSF64Instruction extends F64CvtOpInstruction<I64> {
    @Override
    public I64 op(final F64 val) throws TrapException {
        return val.truncSI64();
    }
}
