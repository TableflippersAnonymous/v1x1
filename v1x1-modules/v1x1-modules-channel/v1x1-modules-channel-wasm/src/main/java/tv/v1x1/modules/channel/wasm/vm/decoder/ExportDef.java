package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportDef {
    private final String name;
    private final ExportDescriptor descriptor;

    public ExportDef(final String name, final ExportDescriptor descriptor) {
        this.name = name;
        this.descriptor = descriptor;
    }

    public static List<ExportDef> decodeVec(final DataInputStream dataInputStream) throws IOException {
        final List<ExportDef> ret = new ArrayList<>();
        final I32 count = I32.decodeU(dataInputStream);
        for(long i = 0; i < count.getValU(); i++)
            ret.add(decode(dataInputStream));
        return ret;
    }

    public static ExportDef decode(final DataInputStream dataInputStream) throws IOException {
        final String name = ModuleDef.decodeString(dataInputStream);
        final ExportDescriptor exportDescriptor = ExportDescriptor.decode(dataInputStream);
        return new ExportDef(name, exportDescriptor);
    }

    public String getName() {
        return name;
    }

    public ExportDescriptor getDescriptor() {
        return descriptor;
    }
}
