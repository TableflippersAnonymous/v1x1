package tv.v1x1.modules.channel.wasm.vm;

import java.io.DataInputStream;
import java.io.IOException;

public enum Mutable {
    CONSTANT, VARIABLE;

    public static Mutable decode(final DataInputStream dataInputStream) throws IOException {
        switch(dataInputStream.readByte()) {
            case 0x00:
                return CONSTANT;
            case 0x01:
                return VARIABLE;
            default:
                throw new DecodeException("Unknown mutable");
        }
    }
}
