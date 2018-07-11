package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.io.DataInputStream;
import java.io.IOException;

public class StartDef {
    private final long funcIdx;

    public StartDef(final long funcIdx) {
        this.funcIdx = funcIdx;
    }

    public static StartDef decode(final DataInputStream dataInputStream) throws IOException {
        return new StartDef(I32.decodeU(dataInputStream).getValU());
    }

    public long getFuncIdx() {
        return funcIdx;
    }
}
