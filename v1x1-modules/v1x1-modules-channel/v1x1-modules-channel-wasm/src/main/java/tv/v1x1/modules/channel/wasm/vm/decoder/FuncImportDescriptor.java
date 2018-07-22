package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;

public class FuncImportDescriptor extends ImportDescriptor {
    private final long typeIdx;

    public FuncImportDescriptor(final long typeIdx) {
        this.typeIdx = typeIdx;
    }

    public static FuncImportDescriptor decode(final DataInputStream dataInputStream) throws IOException {
        final I32 funcIdx = I32.decodeU(dataInputStream);
        return new FuncImportDescriptor(funcIdx.getValU());
    }

    @Override
    public void validate(final Context context) throws ValidationException {
        if(context.getTypes().size() <= typeIdx)
            throw new ValidationException();
    }

    public long getTypeIdx() {
        return typeIdx;
    }
}
