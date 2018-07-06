package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.store;

import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.StoreInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F32;

public class F32StoreInstruction extends StoreInstruction<F32> {
    @Override
    protected Class<F32> getTypeClass() {
        return F32.class;
    }

    @Override
    protected byte[] getBytes(final F32 val) {
        return val.bytes();
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
