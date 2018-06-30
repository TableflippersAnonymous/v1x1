package tv.v1x1.modules.channel.wasm.vm.types;

import tv.v1x1.modules.channel.wasm.vm.stack.StackElement;

public abstract class WebAssemblyType implements StackElement {
    public abstract byte[] bits();

    public byte[] bytes() {
        return swapEndian(bits());
    }

    protected static byte[] swapEndian(final byte[] original) {
        byte[] ret = new byte[original.length];
        for(int i = 0; i < original.length; i++)
            ret[i] = original[original.length - 1 - i];
        return ret;
    }
}
