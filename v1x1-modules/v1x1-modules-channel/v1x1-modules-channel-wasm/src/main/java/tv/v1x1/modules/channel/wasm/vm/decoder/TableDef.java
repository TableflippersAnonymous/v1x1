package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.TableType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TableDef {
    private final TableType tableType;

    public TableDef(final TableType tableType) {
        this.tableType = tableType;
    }

    public static List<TableDef> decodeVec(final DataInputStream dataInputStream) throws IOException {
        final List<TableDef> ret = new ArrayList<>();
        final I32 count = I32.decodeU(dataInputStream);
        for(long i = 0; i < count.getValU(); i++)
            ret.add(decode(dataInputStream));
        return ret;
    }

    private static TableDef decode(final DataInputStream dataInputStream) throws IOException {
        return new TableDef(TableType.decode(dataInputStream));
    }

    public TableType getTableType() {
        return tableType;
    }

    public void validate() throws ValidationException {
        tableType.validate();
    }
}
