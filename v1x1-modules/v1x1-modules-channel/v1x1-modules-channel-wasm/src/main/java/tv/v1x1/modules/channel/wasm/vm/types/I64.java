package tv.v1x1.modules.channel.wasm.vm.types;

import com.google.common.primitives.Longs;
import com.google.common.primitives.UnsignedLong;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;

import java.io.DataInputStream;
import java.io.IOException;

public final class I64 extends IN {
    public static final I64 ZERO = new I64(0);

    private final long val;

    public static I64 decode(final byte[] bits) {
        return new I64(Longs.fromByteArray(bits));
    }

    public static I64 decode(final DataInputStream input) throws IOException {
        long result = 0;
        int shift;
        byte val = -1;
        for(shift = 0; shift < 64 && (val >> 7) != 0; shift += 7) {
            val = input.readByte();
            result |= (val & 0x7f) << shift;
        }
        if (shift < 64 && (val >> 6) != 0)
            result |= (~0 << shift);
        return new I64(result);
    }

    public static I64 decodeU(final DataInputStream input) throws IOException {
        long result = 0;
        byte val = -1;
        for(int shift = 0; shift < 64 && (val >> 7) != 0; shift += 7) {
            val = input.readByte();
            result |= (val & 0x7f) << shift;
        }
        return new I64(result);
    }

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
    public I64 divU(final IN other) throws TrapException {
        if(other.extendU().val == 0)
            throw new TrapException("Div by 0");
        return new I64(Long.divideUnsigned(val, other.extendU().val));
    }

    @Override
    public I64 divS(final IN other) throws TrapException {
        if(other.extendS().val == 0 || (other.extendS().val == -1L && val == Long.MIN_VALUE))
            throw new TrapException("Div by 0");
        return new I64(val / other.extendS().val);
    }

    @Override
    public I64 remU(final IN other) throws TrapException {
        if(other.extendU().val == 0)
            throw new TrapException("Div by 0");
        return new I64(Long.remainderUnsigned(val, other.extendU().val));
    }

    @Override
    public I64 remS(final IN other) throws TrapException {
        if(other.extendS().val == 0)
            throw new TrapException("Div by 0");
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
    public I32 eqz() {
        return bool(val == 0);
    }

    @Override
    public I32 eq(final IN other) {
        return bool(val == other.extendU().val);
    }

    @Override
    public I32 ne(final IN other) {
        return bool(val != other.extendU().val);
    }

    @Override
    public I32 ltU(final IN other) {
        return bool(Long.compareUnsigned(val, other.extendU().val) < 0);
    }

    @Override
    public I32 ltS(final IN other) {
        return bool(val < other.extendS().val);
    }

    @Override
    public I32 gtU(final IN other) {
        return bool(Long.compareUnsigned(val, other.extendU().val) > 0);
    }

    @Override
    public I32 gtS(final IN other) {
        return bool(val > other.extendS().val);
    }

    @Override
    public I32 leU(final IN other) {
        return bool(Long.compareUnsigned(val, other.extendU().val) <= 0);
    }

    @Override
    public I32 leS(final IN other) {
        return bool(val <= other.extendS().val);
    }

    @Override
    public I32 geU(final IN other) {
        return bool(Long.compareUnsigned(val, other.extendU().val) >= 0);
    }

    @Override
    public I32 geS(final IN other) {
        return bool(val >= other.extendS().val);
    }

    @Override
    public F32 convertUF32() {
        return new F32(UnsignedLong.fromLongBits(val).floatValue());
    }

    @Override
    public F32 convertSF32() {
        return new F32((float) val);
    }

    @Override
    public F64 convertUF64() {
        return new F64(UnsignedLong.fromLongBits(val).doubleValue());
    }

    @Override
    public F64 convertSF64() {
        return new F64((double) val);
    }

    @Override
    public F64 reinterpret() {
        return F64.decode(bits());
    }

    public long getVal() {
        return val;
    }

    @Override
    public String toString() {
        return "I64{" +
                "val=" + val +
                '}';
    }

    private I32 bool(final boolean bool) {
        return bool ? I32.ONE : I32.ZERO;
    }
}
