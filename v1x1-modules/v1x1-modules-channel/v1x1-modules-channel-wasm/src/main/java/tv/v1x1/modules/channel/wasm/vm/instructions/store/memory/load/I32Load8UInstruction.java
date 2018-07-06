package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.load;

import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.LoadInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;

public class I32Load8UInstruction extends LoadInstruction {
    @Override
    protected WebAssemblyType convert(final byte[] bytes) {
        return new I32(Byte.toUnsignedInt(bytes[0]));
    }

    @Override
    protected int getWidth() {
        return 8;
    }

    @Override
    protected ValType getType() {
        return ValType.I32;
    }
}
