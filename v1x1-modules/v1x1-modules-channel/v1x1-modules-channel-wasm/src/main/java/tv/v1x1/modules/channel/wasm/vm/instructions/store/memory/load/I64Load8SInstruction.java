package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.load;

import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.LoadInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I64;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;

public class I64Load8SInstruction extends LoadInstruction {
    @Override
    protected WebAssemblyType convert(final byte[] bytes) {
        return new I64(bytes[0]);
    }

    @Override
    protected int getWidth() {
        return 8;
    }

    @Override
    protected ValType getType() {
        return ValType.I64;
    }
}
