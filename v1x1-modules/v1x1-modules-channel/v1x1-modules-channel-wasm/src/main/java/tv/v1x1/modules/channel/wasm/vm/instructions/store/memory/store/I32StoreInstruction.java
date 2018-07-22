package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.store;

import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.StoreInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;

public class I32StoreInstruction extends StoreInstruction<I32> {
    @Override
    protected Class<I32> getTypeClass() {
        return I32.class;
    }

    @Override
    protected byte[] getBytes(final I32 val) {
        return val.bytes();
    }

    @Override
    protected int getWidth() {
        return 32;
    }

    @Override
    protected ValType getType() {
        return ValType.I32;
    }
}
