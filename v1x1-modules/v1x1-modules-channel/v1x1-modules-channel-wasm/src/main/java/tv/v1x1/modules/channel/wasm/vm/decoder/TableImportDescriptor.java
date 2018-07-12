package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.TableType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

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

    @Override
    public void validate(final Context context) throws ValidationException {
        tableType.validate();
    }

    public TableType getTableType() {
        return tableType;
    }
}
