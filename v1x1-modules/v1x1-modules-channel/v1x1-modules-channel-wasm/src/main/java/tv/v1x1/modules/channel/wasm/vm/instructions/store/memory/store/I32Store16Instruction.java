package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.store;

import com.google.common.primitives.Shorts;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.StoreInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;

public class I32Store16Instruction extends StoreInstruction<I32> {
    @Override
    protected Class<I32> getTypeClass() {
        return I32.class;
    }

    @Override
    protected byte[] getBytes(final I32 val) {
        return I32.swapEndian(Shorts.toByteArray((short) (val.getVal() % (1L << 16))));
    }

    @Override
    protected int getWidth() {
        return 16;
    }

    @Override
    protected ValType getType() {
        return ValType.I32;
    }
}
