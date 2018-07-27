package tv.v1x1.modules.channel.wasm.vm.validation;

import tv.v1x1.modules.channel.wasm.vm.decoder.DecodeException;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum ValType {
    I32(tv.v1x1.modules.channel.wasm.vm.types.I32.class, tv.v1x1.modules.channel.wasm.vm.types.I32.ZERO),
    I64(tv.v1x1.modules.channel.wasm.vm.types.I64.class, tv.v1x1.modules.channel.wasm.vm.types.I64.ZERO),
    F32(tv.v1x1.modules.channel.wasm.vm.types.F32.class, tv.v1x1.modules.channel.wasm.vm.types.F32.ZERO),
    F64(tv.v1x1.modules.channel.wasm.vm.types.F64.class, tv.v1x1.modules.channel.wasm.vm.types.F64.ZERO),
    UNKNOWN(null, null);

    private final Class<? extends WebAssemblyType> typeClass;
    private final WebAssemblyType zero;

    ValType(final Class<? extends WebAssemblyType> typeClass, final WebAssemblyType zero) {
        this.typeClass = typeClass;
        this.zero = zero;
    }

    public static Optional<ValType> decodeOptional(final DataInputStream dataInputStream) throws IOException {
        final byte returnTypeByte = dataInputStream.readByte();
        if(returnTypeByte == 0x40)
            return Optional.empty();
        return Optional.of(decode(returnTypeByte));
    }

    public static List<ValType> decodeVec(final DataInputStream dataInputStream) throws IOException {
        final List<ValType> ret = new ArrayList<>();
        final tv.v1x1.modules.channel.wasm.vm.types.I32 count = tv.v1x1.modules.channel.wasm.vm.types.I32.decodeU(dataInputStream);
        for(long i = 0; i < count.getValU(); i++)
            ret.add(decode(dataInputStream));
        return ret;
    }

    public static ValType decode(final DataInputStream dataInputStream) throws IOException {
        return decode(dataInputStream.readByte());
    }

    private static ValType decode(final byte returnTypeByte) throws DecodeException {
        switch(returnTypeByte) {
            case 0x7F: return I32;
            case 0x7E: return I64;
            case 0x7D: return F32;
            case 0x7C: return F64;
            default: throw new DecodeException("Invalid valtype");
        }
    }

    public Class<? extends WebAssemblyType> getTypeClass() {
        return typeClass;
    }

    public WebAssemblyType getZero() {
        return zero;
    }
}
