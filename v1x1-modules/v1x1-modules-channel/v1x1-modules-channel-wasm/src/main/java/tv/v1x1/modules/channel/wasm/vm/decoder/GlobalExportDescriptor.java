package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;

public class GlobalExportDescriptor extends ExportDescriptor {
    private final long globalIdx;

    public GlobalExportDescriptor(final long globalIdx) {
        this.globalIdx = globalIdx;
    }

    public static GlobalExportDescriptor decode(final DataInputStream dataInputStream) throws IOException {
        final I32 globalIdx = I32.decodeU(dataInputStream);
        return new GlobalExportDescriptor(globalIdx.getValU());
    }

    @Override
    public void validate(final Context context) throws ValidationException {
        if(context.getGlobals().size() <= globalIdx)
            throw new ValidationException();
    }

    public long getGlobalIdx() {
        return globalIdx;
    }
}
