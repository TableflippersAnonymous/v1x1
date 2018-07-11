package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImportDef {
    private final String module;
    private final String name;
    private final ImportDescriptor descriptor;

    public ImportDef(final String module, final String name, final ImportDescriptor descriptor) {
        this.module = module;
        this.name = name;
        this.descriptor = descriptor;
    }

    public static List<ImportDef> decodeVec(final DataInputStream dataInputStream) throws IOException {
        final List<ImportDef> ret = new ArrayList<>();
        final I32 count = I32.decodeU(dataInputStream);
        for(long i = 0; i < count.getValU(); i++)
            ret.add(decode(dataInputStream));
        return ret;
    }

    public static ImportDef decode(final DataInputStream dataInputStream) throws IOException {
        final String module = ModuleDef.decodeString(dataInputStream);
        final String name = ModuleDef.decodeString(dataInputStream);
        final ImportDescriptor importDescriptor = ImportDescriptor.decode(dataInputStream);
        return new ImportDef(module, name, importDescriptor);
    }

    public String getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public ImportDescriptor getDescriptor() {
        return descriptor;
    }
}
