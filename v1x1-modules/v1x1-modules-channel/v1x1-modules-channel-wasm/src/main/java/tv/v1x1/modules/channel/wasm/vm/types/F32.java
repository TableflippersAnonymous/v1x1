package tv.v1x1.modules.channel.wasm.vm.types;

import com.google.common.primitives.Ints;

public final class F32 extends FN {
    public static F32 decode(final byte[] bits) {
        return new F32(Float.intBitsToFloat(Ints.fromByteArray(bits)));
    }

    private final float val;

    public F32(final float val) {
        this.val = val;
    }

    @Override
    public byte[] bits() {
        return Ints.toByteArray(Float.floatToRawIntBits(val));
    }
}
