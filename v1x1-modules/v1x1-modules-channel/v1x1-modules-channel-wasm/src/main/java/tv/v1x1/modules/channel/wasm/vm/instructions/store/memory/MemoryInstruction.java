package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory;

import com.google.common.math.IntMath;
import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class MemoryInstruction extends Instruction {
    protected I32 offset;
    protected I32 align;

    @Override
    public void decode(final DataInputStream dataInputStream) throws IOException {
        offset = I32.decodeU(dataInputStream);
        align = I32.decodeU(dataInputStream);
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        if(context.getMemories().isEmpty())
            throw new ValidationException();
        if(align.getVal() > 3 || align.getVal() < 0 || offset.getVal() < 0)
            throw new ValidationException();
        if(IntMath.pow(2, align.getVal()) > getWidth() / 8)
            throw new ValidationException();
        stack.popOperand(ValType.I32);
    }

    protected abstract int getWidth();
    protected abstract ValType getType();
}
