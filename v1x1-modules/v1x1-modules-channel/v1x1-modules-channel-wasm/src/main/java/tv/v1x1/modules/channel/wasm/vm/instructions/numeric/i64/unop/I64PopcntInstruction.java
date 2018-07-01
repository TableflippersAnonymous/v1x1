package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.unop;

import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.I64UnOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I64;

public class I64PopcntInstruction extends I64UnOpInstruction {
    @Override
    public I64 op(final I64 val) throws TrapException {
        return val.popcnt();
    }
}
