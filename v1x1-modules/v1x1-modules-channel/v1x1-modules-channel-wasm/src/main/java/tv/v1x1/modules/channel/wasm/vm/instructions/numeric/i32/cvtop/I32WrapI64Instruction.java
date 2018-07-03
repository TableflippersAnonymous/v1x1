package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.cvtop;

import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.I64CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.I64;

public class I32WrapI64Instruction extends I64CvtOpInstruction<I32> {
    @Override
    public I32 op(final I64 val) {
        return val.wrap();
    }
}
