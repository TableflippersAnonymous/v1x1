package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemoryDef {
    private final MemoryType memoryType;

    public MemoryDef(final MemoryType memoryType) {
        this.memoryType = memoryType;
    }

    public static List<MemoryDef> decodeVec(final DataInputStream dataInputStream) throws IOException {
        final List<MemoryDef> ret = new ArrayList<>();
        final I32 count = I32.decodeU(dataInputStream);
        for(long i = 0; i < count.getValU(); i++)
            ret.add(decode(dataInputStream));
        return ret;
    }

    private static MemoryDef decode(final DataInputStream dataInputStream) throws IOException {
        return new MemoryDef(MemoryType.decode(dataInputStream));
    }

    public MemoryType getMemoryType() {
        return memoryType;
    }
}
