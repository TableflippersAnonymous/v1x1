package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.store;

import com.google.common.primitives.Shorts;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.StoreInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I64;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;

public class I64Store16Instruction extends StoreInstruction<I64> {
    @Override
    protected Class<I64> getTypeClass() {
        return I64.class;
    }

    @Override
    protected byte[] getBytes(final I64 val) {
        return I64.swapEndian(Shorts.toByteArray((short) (val.getVal() % (1L << 16))));
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
