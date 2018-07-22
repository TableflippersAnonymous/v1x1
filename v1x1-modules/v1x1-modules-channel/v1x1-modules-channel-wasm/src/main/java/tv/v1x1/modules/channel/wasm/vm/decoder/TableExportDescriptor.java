package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

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

    @Override
    public void validate(final Context context) throws ValidationException {
        if(context.getTables().size() <= tableIdx)
            throw new ValidationException();
    }

    public long getTableIdx() {
        return tableIdx;
    }
}
