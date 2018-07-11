package tv.v1x1.modules.channel.wasm.vm;

import java.io.DataInputStream;
import java.io.IOException;

public enum ElemType {
    ANY_FUNC;

    public static ElemType decode(final DataInputStream dataInputStream) throws IOException {
        switch(dataInputStream.readByte()) {
            case 0x70:
                return ANY_FUNC;
            default:
                throw new DecodeException("Invalid elemtype");
        }
    }
}
