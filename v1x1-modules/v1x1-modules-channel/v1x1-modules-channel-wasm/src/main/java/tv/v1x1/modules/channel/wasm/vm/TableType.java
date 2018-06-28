package tv.v1x1.modules.channel.wasm.vm;

public class TableType {
    private Limits limits;
    private ElemType elemType;

    // https://webassembly.github.io/spec/core/valid/types.html#table-types
    public boolean validate() {
        return limits.validate();
    }
}
