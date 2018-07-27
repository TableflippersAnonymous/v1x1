package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.unop;

import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.F64UnOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.types.F64;

public class F64SqrtInstruction extends F64UnOpInstruction {
    @Override
    public F64 op(final F64 val) throws TrapException {
        return val.sqrt();
    }
}
