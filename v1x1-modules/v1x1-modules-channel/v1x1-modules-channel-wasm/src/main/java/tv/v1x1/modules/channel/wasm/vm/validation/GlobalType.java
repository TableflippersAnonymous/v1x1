package tv.v1x1.modules.channel.wasm.vm.validation;

import tv.v1x1.modules.channel.wasm.vm.store.Mutable;

import java.io.DataInputStream;
import java.io.IOException;

public class GlobalType {
    private Mutable mutable;
    private ValType valType;

    public GlobalType(final Mutable mutable, final ValType valType) {
        this.mutable = mutable;
        this.valType = valType;
    }

    public static GlobalType decode(final DataInputStream dataInputStream) throws IOException {
        final ValType valType = ValType.decode(dataInputStream);
        final Mutable mutable = Mutable.decode(dataInputStream);
        return new GlobalType(mutable, valType);
    }

    public Mutable getMutable() {
        return mutable;
    }

    public ValType getValType() {
        return valType;
    }

    // https://webassembly.github.io/spec/core/valid/types.html#global-types
    public void validate() throws ValidationException {
        /* No action */
    }
}
