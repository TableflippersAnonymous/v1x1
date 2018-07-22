package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.store;

import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.StoreInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F64;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;

public class F64StoreInstruction extends StoreInstruction<F64> {
    @Override
    protected Class<F64> getTypeClass() {
        return F64.class;
    }

    @Override
    protected byte[] getBytes(final F64 val) {
        return val.bytes();
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
