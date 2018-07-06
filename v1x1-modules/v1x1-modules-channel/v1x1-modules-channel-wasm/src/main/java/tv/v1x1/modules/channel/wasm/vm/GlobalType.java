package tv.v1x1.modules.channel.wasm.vm;

public class GlobalType {
    private Mutable mutable;
    private ValType valType;

    public GlobalType(final Mutable mutable, final ValType valType) {
        this.mutable = mutable;
        this.valType = valType;
    }

    public Mutable getMutable() {
        return mutable;
    }

    public ValType getValType() {
        return valType;
    }

    // https://webassembly.github.io/spec/core/valid/types.html#global-types
    public boolean validate() {
        return true;
    }
}
