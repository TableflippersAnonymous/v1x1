package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

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

    @Override
    public void validate(final Context context) throws ValidationException {
        memoryType.validate();
    }

    public MemoryType getMemoryType() {
        return memoryType;
    }
}
