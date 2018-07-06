package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.store;

import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.StoreInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I64;

public class I64Store8Instruction extends StoreInstruction<I64> {
    @Override
    protected Class<I64> getTypeClass() {
        return I64.class;
    }

    @Override
    protected byte[] getBytes(final I64 val) {
        return new byte[] {(byte) (val.getVal() % (1L << 8))};
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
