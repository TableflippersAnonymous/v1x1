package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.cvtop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.I32CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.I64;

public class I64ExtendSI32Instruction extends I32CvtOpInstruction<I64> {
    @Override
    public I64 op(final I32 val) throws TrapException {
        return val.extendS();
    }
}
