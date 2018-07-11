package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.TableType;

import java.io.DataInputStream;
import java.io.IOException;

public class TableImportDescriptor extends ImportDescriptor {
    private final TableType tableType;

    public TableImportDescriptor(final TableType tableType) {
        this.tableType = tableType;
    }

    public static TableImportDescriptor decode(final DataInputStream dataInputStream) throws IOException {
        final TableType tableType = TableType.decode(dataInputStream);
        return new TableImportDescriptor(tableType);
    }

    public TableType getTableType() {
        return tableType;
    }
}
