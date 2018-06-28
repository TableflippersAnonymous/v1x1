package tv.v1x1.modules.channel.wasm.vm.types;

public abstract class WebAssemblyType {
    public abstract byte[] bits();

    public byte[] bytes() {
        return littleEndian(bits());
    }

    private static byte[] littleEndian(final byte[] original) {
        byte[] ret = new byte[original.length];
        for(int i = 0; i < original.length; i++)
            ret[i] = original[original.length - 1 - i];
        return ret;
    }
}
