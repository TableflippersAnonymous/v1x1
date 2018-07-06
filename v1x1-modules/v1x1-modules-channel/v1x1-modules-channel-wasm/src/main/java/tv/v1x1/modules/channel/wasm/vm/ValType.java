package tv.v1x1.modules.channel.wasm.vm;

import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;

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

    public Class<? extends WebAssemblyType> getTypeClass() {
        return typeClass;
    }
}
