package tv.v1x1.modules.channel.wasm.vm.types;

import com.google.common.primitives.Longs;

public final class I64 extends IN {
    public static final I64 ZERO = new I64(0);
    public static final I64 ONE = new I64(1);

    public static I64 decode(final byte[] bits) {
        return new I64(Longs.fromByteArray(bits));
    }

    private final long val;

    public I64(final long val) {
        this.val = val;
    }

    @Override
    public byte[] bits() {
        return Longs.toByteArray(val);
    }

    @Override
    public I64 extendU() {
        return this;
    }

    @Override
    public I64 extendS() {
        return this;
    }

    @Override
    public I32 wrap() {
        return new I32((int) (val % (1L << 32)));
    }

    @Override
    public I64 add(final IN other) {
        return new I64(val + other.extendU().val);
    }

    @Override
    public I64 sub(final IN other) {
        return new I64(val - other.extendU().val);
    }

    @Override
    public I64 mul(final IN other) {
        return new I64(val * other.extendU().val);
    }

    @Override
    public I64 divU(final IN other) {
        return new I64(Long.divideUnsigned(val, other.extendU().val));
    }

    @Override
    public I64 divS(final IN other) {
        return new I64(val / other.extendS().val);
    }

    @Override
    public I64 remU(final IN other) {
        return new I64(Long.remainderUnsigned(val, other.extendU().val));
    }

    @Override
    public I64 remS(final IN other) {
        return new I64(val % other.extendS().val);
    }

    @Override
    public I64 and(final IN other) {
        return new I64(val & other.extendU().val);
    }

    @Override
    public I64 or(final IN other) {
        return new I64(val | other.extendU().val);
    }

    @Override
    public I64 xor(final IN other) {
        return new I64(val ^ other.extendU().val);
    }

    @Override
    public I64 shl(final IN other) {
        return new I64(val << other.extendU().val);
    }

    @Override
    public I64 shrU(final IN other) {
        return new I64(val >>> other.extendU().val);
    }

    @Override
    public I64 shrS(final IN other) {
        return new I64(val >> other.extendU().val);
    }

    @Override
    public I64 rotl(final IN other) {
        return new I64(Long.rotateLeft(val, (int) (other.extendU().val % 64)));
    }

    @Override
    public I64 rotr(final IN other) {
        return new I64(Long.rotateRight(val, (int) (other.extendU().val % 64)));
    }

    @Override
    public I64 clz() {
        return new I64(Long.numberOfLeadingZeros(val));
    }

    @Override
    public I64 ctz() {
        return new I64(Long.numberOfTrailingZeros(val));
    }

    @Override
    public I64 popcnt() {
        return new I64(Long.bitCount(val));
    }

    @Override
    public I64 eqz() {
        return bool(val == 0);
    }

    @Override
    public I64 eq(final IN other) {
        return bool(val == other.extendU().val);
    }

    @Override
    public I64 ne(final IN other) {
        return bool(val != other.extendU().val);
    }

    @Override
    public I64 ltU(final IN other) {
        return bool(Long.compareUnsigned(val, other.extendU().val) < 0);
    }

    @Override
    public I64 ltS(final IN other) {
        return bool(val < other.extendS().val);
    }

    @Override
    public I64 gtU(final IN other) {
        return bool(Long.compareUnsigned(val, other.extendU().val) > 0);
    }

    @Override
    public I64 gtS(final IN other) {
        return bool(val > other.extendS().val);
    }

    @Override
    public I64 leU(final IN other) {
        return bool(Long.compareUnsigned(val, other.extendU().val) <= 0);
    }

    @Override
    public I64 leS(final IN other) {
        return bool(val <= other.extendS().val);
    }

    @Override
    public I64 geU(final IN other) {
        return bool(Long.compareUnsigned(val, other.extendU().val) >= 0);
    }

    @Override
    public I64 geS(final IN other) {
        return bool(val >= other.extendS().val);
    }

    @Override
    public FN reinterpret() {
        return F64.decode(bits());
    }

    private I64 bool(final boolean bool) {
        return bool ? I64.ONE : I64.ZERO;
    }
}
