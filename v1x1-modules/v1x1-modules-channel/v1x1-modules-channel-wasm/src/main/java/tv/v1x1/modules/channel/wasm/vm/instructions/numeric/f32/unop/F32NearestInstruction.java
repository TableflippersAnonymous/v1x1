package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.unop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.F32UnOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F32;

public class F32NearestInstruction extends F32UnOpInstruction {
    @Override
    public F32 op(final F32 val) throws TrapException {
        return val.nearest();
    }
}