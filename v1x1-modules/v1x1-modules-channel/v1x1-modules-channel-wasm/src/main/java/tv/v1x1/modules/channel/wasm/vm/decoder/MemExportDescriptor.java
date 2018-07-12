package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;

public class MemExportDescriptor extends ExportDescriptor {
    private final long memIdx;

    public MemExportDescriptor(final long memIdx) {
        this.memIdx = memIdx;
    }

    public static MemExportDescriptor decode(final DataInputStream dataInputStream) throws IOException {
        final I32 memIdx = I32.decodeU(dataInputStream);
        return new MemExportDescriptor(memIdx.getValU());
    }

    @Override
    public void validate(final Context context) throws ValidationException {
        if(context.getMemories().size() <= memIdx)
            throw new ValidationException();
    }

    public long getMemIdx() {
        return memIdx;
    }
}
