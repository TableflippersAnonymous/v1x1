package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.load;

import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.LoadInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F32;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;

public class F32LoadInstruction extends LoadInstruction {
    @Override
    protected WebAssemblyType convert(final byte[] bytes) {
        return F32.decode(F32.swapEndian(bytes));
    }

    @Override
    protected int getWidth() {
        return 32;
    }

    @Override
    protected ValType getType() {
        return ValType.F32;
    }
}
