package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

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

    public long getMemIdx() {
        return memIdx;
    }
}
