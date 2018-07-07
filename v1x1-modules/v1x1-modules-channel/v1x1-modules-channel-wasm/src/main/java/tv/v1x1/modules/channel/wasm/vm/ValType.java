package tv.v1x1.modules.channel.wasm.vm;

import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

public enum ValType {
    I32(tv.v1x1.modules.channel.wasm.vm.types.I32.class),
    I64(tv.v1x1.modules.channel.wasm.vm.types.I64.class),
    F32(tv.v1x1.modules.channel.wasm.vm.types.F32.class),
    F64(tv.v1x1.modules.channel.wasm.vm.types.F64.class),
    UNKNOWN(null);

    private final Class<? extends WebAssemblyType> typeClass;

    ValType(final Class<? extends WebAssemblyType> typeClass) {
        this.typeClass = typeClass;
    }

    public static Optional<ValType> decodeOptional(final DataInputStream dataInputStream) throws IOException {
        final byte returnTypeByte = dataInputStream.readByte();
        if(returnTypeByte == 0x40)
            return Optional.empty();
        return Optional.of(decode(returnTypeByte));
    }

    private static ValType decode(final byte returnTypeByte) throws DecodeException {
        switch(returnTypeByte) {
            case 0x7F: return I32;
            case 0x7E: return I64;
            case 0x7D: return F32;
            case 0x7C: return F64;
        }
        throw new DecodeException();
    }

    public Class<? extends WebAssemblyType> getTypeClass() {
        return typeClass;
    }
}
