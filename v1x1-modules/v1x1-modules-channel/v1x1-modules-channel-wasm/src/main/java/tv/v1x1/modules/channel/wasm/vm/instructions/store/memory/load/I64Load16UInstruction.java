package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.load;

import com.google.common.primitives.Shorts;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.LoadInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I64;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;

public class I64Load16UInstruction extends LoadInstruction {
    @Override
    protected WebAssemblyType convert(final byte[] bytes) {
        return new I64(Short.toUnsignedLong(Shorts.fromByteArray(I64.swapEndian(bytes))));
    }

    @Override
    protected int getWidth() {
        return 16;
    }

    @Override
    protected ValType getType() {
        return ValType.I64;
    }
}
