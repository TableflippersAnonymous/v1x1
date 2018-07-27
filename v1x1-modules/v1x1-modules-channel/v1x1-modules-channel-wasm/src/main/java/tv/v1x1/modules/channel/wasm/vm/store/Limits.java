package tv.v1x1.modules.channel.wasm.vm.store;

import tv.v1x1.modules.channel.wasm.vm.decoder.DecodeException;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

public class Limits {
    private final I32 min;
    private final Optional<I32> max;

    public Limits(final I32 min, final Optional<I32> max) {
        this.min = min;
        this.max = max;
    }

    public static Limits decode(final DataInputStream dataInputStream) throws IOException {
        switch(dataInputStream.readByte()) {
            case 0x00:
                return new Limits(I32.decodeU(dataInputStream), Optional.empty());
            case 0x01:
                return new Limits(I32.decodeU(dataInputStream), Optional.of(I32.decodeU(dataInputStream)));
            default:
                throw new DecodeException("Invalid limits");
        }
    }

    // https://webassembly.github.io/spec/core/valid/types.html#limits
    public void validate() throws ValidationException {
        if(max.map(i32 -> min.leU(i32) == I32.ONE).orElse(true))
            return;
        throw new ValidationException();
    }

    public I32 getMin() {
        return min;
    }

    public Optional<I32> getMax() {
        return max;
    }
}
