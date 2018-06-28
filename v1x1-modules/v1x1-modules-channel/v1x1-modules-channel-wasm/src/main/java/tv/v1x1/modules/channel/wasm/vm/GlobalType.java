package tv.v1x1.modules.channel.wasm.vm;

public class GlobalType {
    private Mutable mutable;
    private ValType valType;

    // https://webassembly.github.io/spec/core/valid/types.html#global-types
    public boolean validate() {
        return true;
    }
}
