package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.io.DataInputStream;
import java.io.IOException;

public class TableExportDescriptor extends ExportDescriptor {
    private final long tableIdx;

    public TableExportDescriptor(final long tableIdx) {
        this.tableIdx = tableIdx;
    }

    public static TableExportDescriptor decode(final DataInputStream dataInputStream) throws IOException {
        final I32 tableIdx = I32.decodeU(dataInputStream);
        return new TableExportDescriptor(tableIdx.getValU());
    }

    public long getTableIdx() {
        return tableIdx;
    }
}
