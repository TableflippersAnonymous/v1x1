package tv.v1x1.modules.channel.wasm.vm;

import java.io.DataInputStream;
import java.io.IOException;

public class TableType {
    private final Limits limits;
    private final ElemType elemType;

    public TableType(final Limits limits, final ElemType elemType) {
        this.limits = limits;
        this.elemType = elemType;
    }

    public static TableType decode(final DataInputStream dataInputStream) throws IOException {
        final ElemType elemType = ElemType.decode(dataInputStream);
        final Limits limits = Limits.decode(dataInputStream);
        return new TableType(limits, elemType);
    }

    // https://webassembly.github.io/spec/core/valid/types.html#table-types
    public boolean validate() {
        return limits.validate();
    }

    public Limits getLimits() {
        return limits;
    }

    public ElemType getElemType() {
        return elemType;
    }
}
