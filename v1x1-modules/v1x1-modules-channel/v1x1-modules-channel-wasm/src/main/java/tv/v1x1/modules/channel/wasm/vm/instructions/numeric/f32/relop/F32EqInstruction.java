package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.relop;

import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.F32RelOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.types.F32;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

public class F32EqInstruction extends F32RelOpInstruction {
    @Override
    public I32 op(final F32 val1, final F32 val2) throws TrapException {
        return val1.eq(val2);
    }
}
