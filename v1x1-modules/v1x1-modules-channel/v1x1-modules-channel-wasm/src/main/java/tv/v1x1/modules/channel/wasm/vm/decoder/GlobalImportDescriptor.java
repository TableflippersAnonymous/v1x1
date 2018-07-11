package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.GlobalType;

import java.io.DataInputStream;
import java.io.IOException;

public class GlobalImportDescriptor extends ImportDescriptor {
    private final GlobalType globalType;

    public GlobalImportDescriptor(final GlobalType globalType) {
        this.globalType = globalType;
    }

    public static GlobalImportDescriptor decode(final DataInputStream dataInputStream) throws IOException {
        final GlobalType globalType = GlobalType.decode(dataInputStream);
        return new GlobalImportDescriptor(globalType);
    }

    public GlobalType getGlobalType() {
        return globalType;
    }
}
