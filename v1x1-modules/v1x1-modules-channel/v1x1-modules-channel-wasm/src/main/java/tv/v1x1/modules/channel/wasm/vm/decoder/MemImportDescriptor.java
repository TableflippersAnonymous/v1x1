package tv.v1x1.modules.channel.wasm.vm.decoder;

import java.io.DataInputStream;
import java.io.IOException;

public class MemImportDescriptor extends ImportDescriptor {
    private final MemoryType memoryType;

    public MemImportDescriptor(final MemoryType memoryType) {
        this.memoryType = memoryType;
    }

    public static MemImportDescriptor decode(final DataInputStream dataInputStream) throws IOException {
        final MemoryType memoryType = MemoryType.decode(dataInputStream);
        return new MemImportDescriptor(memoryType);
    }

    public MemoryType getMemoryType() {
        return memoryType;
    }
}
