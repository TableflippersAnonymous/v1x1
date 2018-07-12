package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;

public class FuncExportDescriptor extends ExportDescriptor {
    private final long funcIdx;

    public FuncExportDescriptor(final long funcIdx) {
        this.funcIdx = funcIdx;
    }

    public static FuncExportDescriptor decode(final DataInputStream dataInputStream) throws IOException {
        final I32 funcIdx = I32.decodeU(dataInputStream);
        return new FuncExportDescriptor(funcIdx.getValU());
    }

    @Override
    public void validate(final Context context) throws ValidationException {
        if(context.getFuncs().size() <= funcIdx)
            throw new ValidationException();
    }

    public long getFuncIdx() {
        return funcIdx;
    }
}
