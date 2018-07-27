package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.binop;

import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.I32BinOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

public class I32SubInstruction extends I32BinOpInstruction {
    @Override
    public I32 op(final I32 val1, final I32 val2) {
        return val1.sub(val2);
    }
}
