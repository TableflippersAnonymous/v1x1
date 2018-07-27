package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.load;

import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.LoadInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F64;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;

public class F64LoadInstruction extends LoadInstruction {
    @Override
    protected WebAssemblyType convert(final byte[] bytes) {
        return F64.decode(F64.swapEndian(bytes));
    }

    @Override
    protected int getWidth() {
        return 64;
    }

    @Override
    protected ValType getType() {
        return ValType.F64;
    }
}
