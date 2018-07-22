package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.store;

import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.StoreInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I64;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;

public class I64Store32Instruction extends StoreInstruction<I64> {
    @Override
    protected Class<I64> getTypeClass() {
        return I64.class;
    }

    @Override
    protected byte[] getBytes(final I64 val) {
        return val.wrap().bytes();
    }

    @Override
    protected int getWidth() {
        return 32;
    }

    @Override
    protected ValType getType() {
        return ValType.I64;
    }
}
