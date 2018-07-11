package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.io.DataInputStream;
import java.io.IOException;

public class FuncImportDescriptor extends ImportDescriptor {
    private final long funcIdx;

    public FuncImportDescriptor(final long funcIdx) {
        this.funcIdx = funcIdx;
    }

    public static FuncImportDescriptor decode(final DataInputStream dataInputStream) throws IOException {
        final I32 funcIdx = I32.decodeU(dataInputStream);
        return new FuncImportDescriptor(funcIdx.getValU());
    }

    public long getFuncIdx() {
        return funcIdx;
    }
}
