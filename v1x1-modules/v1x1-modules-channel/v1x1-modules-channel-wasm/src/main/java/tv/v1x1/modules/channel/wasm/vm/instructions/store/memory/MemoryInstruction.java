package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory;

import com.google.common.math.IntMath;
import tv.v1x1.modules.channel.wasm.vm.runtime.Instruction;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;
import tv.v1x1.modules.channel.wasm.vm.validation.WebAssemblyValidationStack;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class MemoryInstruction extends Instruction {
    protected I32 offset;
    protected I32 align;

    @Override
    public void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException {
        align = I32.decodeU(dataInputStream);
        offset = I32.decodeU(dataInputStream);
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        if(context.getMemories().isEmpty())
            throw new ValidationException();
        if(align.getValU() > 3)
            throw new ValidationException();
        if(IntMath.pow(2, (int) align.getValU()) > getWidth() / 8)
            throw new ValidationException();
        stack.popOperand(ValType.I32);
    }

    protected abstract int getWidth();
    protected abstract ValType getType();
}
