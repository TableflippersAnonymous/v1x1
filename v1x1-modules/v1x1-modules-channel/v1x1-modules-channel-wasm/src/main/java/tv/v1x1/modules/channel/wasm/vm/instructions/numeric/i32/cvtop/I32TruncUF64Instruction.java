package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.cvtop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.F64CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F64;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

public class I32TruncUF64Instruction extends F64CvtOpInstruction<I32> {
    @Override
    public I32 op(final F64 val) throws TrapException {
        return val.truncUI32();
    }
}
