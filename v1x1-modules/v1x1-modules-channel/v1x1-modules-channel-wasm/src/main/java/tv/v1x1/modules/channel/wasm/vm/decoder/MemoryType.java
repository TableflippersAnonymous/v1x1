package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.store.Limits;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;

public class MemoryType {
    private final Limits limits;

    public MemoryType(final Limits limits) {
        this.limits = limits;
    }

    public static MemoryType decode(final DataInputStream dataInputStream) throws IOException {
        return new MemoryType(Limits.decode(dataInputStream));
    }

    public Limits getLimits() {
        return limits;
    }

    public void validate() throws ValidationException {
        limits.validate();
    }
}
