package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.load;

import com.google.common.primitives.Shorts;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.LoadInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;

public class I32Load16SInstruction extends LoadInstruction {
    @Override
    protected WebAssemblyType convert(final byte[] bytes) {
        return new I32(Shorts.fromByteArray(I32.swapEndian(bytes)));
    }

    @Override
    protected int getWidth() {
        return 16;
    }

    @Override
    protected ValType getType() {
        return ValType.I32;
    }
}
