package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.relop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.F64RelOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F64;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

public class F64GeInstruction extends F64RelOpInstruction {
    @Override
    public I32 op(final F64 val1, final F64 val2) throws TrapException {
        return val1.ge(val2);
    }
}
