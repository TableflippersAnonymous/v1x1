package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.cvtop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.F32CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F32;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

public class I32ReinterpretF32Instruction extends F32CvtOpInstruction<I32> {
    @Override
    public I32 op(final F32 val) throws TrapException {
        return val.reinterpret();
    }
}
