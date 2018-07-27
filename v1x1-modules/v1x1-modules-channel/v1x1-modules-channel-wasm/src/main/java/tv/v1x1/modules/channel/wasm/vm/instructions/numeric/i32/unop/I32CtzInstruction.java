package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.unop;

import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.I32UnOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

public class I32CtzInstruction extends I32UnOpInstruction {
    @Override
    public I32 op(final I32 val) throws TrapException {
        return val.ctz();
    }
}
