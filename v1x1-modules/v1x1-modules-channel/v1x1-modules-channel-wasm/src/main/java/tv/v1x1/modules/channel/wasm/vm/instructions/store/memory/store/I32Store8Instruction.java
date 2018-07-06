package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.store;

import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.StoreInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

public class I32Store8Instruction extends StoreInstruction<I32> {
    @Override
    protected Class<I32> getTypeClass() {
        return I32.class;
    }

    @Override
    protected byte[] getBytes(final I32 val) {
        return new byte[] {(byte) (val.getVal() % (1L << 8))};
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
