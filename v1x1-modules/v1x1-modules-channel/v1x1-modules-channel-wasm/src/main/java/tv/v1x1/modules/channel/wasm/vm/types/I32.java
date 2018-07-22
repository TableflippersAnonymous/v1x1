package tv.v1x1.modules.channel.wasm.vm.types;

import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedInteger;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;

import java.io.DataInputStream;
import java.io.IOException;

public final class I32 extends IN {
    public static final I32 ZERO = new I32(0);
    public static final I32 ONE = new I32(1);

    public static I32 decode(final byte[] bits) {
        return new I32(Ints.fromByteArray(bits));
    }

    public static I32 decode(final DataInputStream input) throws IOException {
        int result = 0, shift;
        byte val = -1;
        for(shift = 0; shift < 32 && (val >> 7) != 0; shift += 7) {
            val = input.readByte();
            result |= (val & 0x7f) << shift;
        }
        if (shift < 32 && (val >> 6) != 0)
            result |= (~0 << shift);
        return new I32(result);
    }

    public static I32 decodeU(final DataInputStream input) throws IOException {
        int result = 0;
        byte val = -1;
        for(int shift = 0; shift < 32 && (val >> 7) != 0; shift += 7) {
            val = input.readByte();
            result |= (val & 0x7f) << shift;
        }
        return new I32(result);
    }

    private final int val;

    public I32(final int val) {
        this.val = val;
    }

    @Override
    public byte[] bits() {
        return Ints.toByteArray(val);
    }

    @Override
    public I64 extendU() {
        return new I64(Integer.toUnsignedLong(val));
    }

    @Override
    public I64 extendS() {
        return new I64(val);
    }

    @Override
    public I32 wrap() {
        return this;
    }

    @Override
    public I32 add(final IN other) {
        return new I32(val + other.wrap().val);
    }

    @Override
    public I32 sub(final IN other) {
        return new I32(val - other.wrap().val);
    }

    @Override
    public I32 mul(final IN other) {
        return new I32(val * other.wrap().val);
    }

    @Override
    public I32 divU(final IN other) throws TrapException {
        if(other.wrap().val == 0)
            throw new TrapException("Div by 0");
        return new I32(Integer.divideUnsigned(val, other.wrap().val));
    }

    @Override
    public I32 divS(final IN other) throws TrapException {
        if(other.wrap().val == 0 || (other.wrap().val == -1L && val == Integer.MIN_VALUE))
            throw new TrapException("Div by 0");
        return new I32(val / other.wrap().val);
    }

    @Override
    public I32 remU(final IN other) throws TrapException {
        if(other.wrap().val == 0)
            throw new TrapException("Div by 0");
        return new I32(Integer.remainderUnsigned(val, other.wrap().val));
    }

    @Override
    public I32 remS(final IN other) throws TrapException {
        if(other.wrap().val == 0)
            throw new TrapException("Div by 0");
        return new I32(val % other.wrap().val);
    }

    @Override
    public I32 and(final IN other) {
        return new I32(val & other.wrap().val);
    }

    @Override
    public I32 or(final IN other) {
        return new I32(val | other.wrap().val);
    }

    @Override
    public I32 xor(final IN other) {
        return new I32(val ^ other.wrap().val);
    }

    @Override
    public I32 shl(final IN other) {
        return new I32(val << other.wrap().val);
    }

    @Override
    public I32 shrU(final IN other) {
        return new I32(val >>> other.wrap().val);
    }

    @Override
    public I32 shrS(final IN other) {
        return new I32(val >> other.wrap().val);
    }

    @Override
    public I32 rotl(final IN other) {
        return new I32(Integer.rotateLeft(val, (int) (other.wrap().val % 64)));
    }

    @Override
    public I32 rotr(final IN other) {
        return new I32(Integer.rotateRight(val, (int) (other.wrap().val % 64)));
    }

    @Override
    public I32 clz() {
        return new I32(Integer.numberOfLeadingZeros(val));
    }

    @Override
    public I32 ctz() {
        return new I32(Integer.numberOfTrailingZeros(val));
    }

    @Override
    public I32 popcnt() {
        return new I32(Integer.bitCount(val));
    }

    @Override
    public I32 eqz() {
        return bool(val == 0);
    }

    @Override
    public I32 eq(final IN other) {
        return bool(val == other.wrap().val);
    }

    @Override
    public I32 ne(final IN other) {
        return bool(val != other.wrap().val);
    }

    @Override
    public I32 ltU(final IN other) {
        return bool(Integer.compareUnsigned(val, other.wrap().val) < 0);
    }

    @Override
    public I32 ltS(final IN other) {
        return bool(val < other.wrap().val);
    }

    @Override
    public I32 gtU(final IN other) {
        return bool(Integer.compareUnsigned(val, other.wrap().val) > 0);
    }

    @Override
    public I32 gtS(final IN other) {
        return bool(val > other.wrap().val);
    }

    @Override
    public I32 leU(final IN other) {
        return bool(Integer.compareUnsigned(val, other.wrap().val) <= 0);
    }

    @Override
    public I32 leS(final IN other) {
        return bool(val <= other.wrap().val);
    }

    @Override
    public I32 geU(final IN other) {
        return bool(Integer.compareUnsigned(val, other.wrap().val) >= 0);
    }

    @Override
    public I32 geS(final IN other) {
        return bool(val >= other.wrap().val);
    }

    @Override
    public F32 convertUF32() {
        return new F32(UnsignedInteger.fromIntBits(val).floatValue());
    }

    @Override
    public F32 convertSF32() {
        return new F32((float) val);
    }

    @Override
    public F64 convertUF64() {
        return new F64(UnsignedInteger.fromIntBits(val).doubleValue());
    }

    @Override
    public F64 convertSF64() {
        return new F64((double) val);
    }

    @Override
    public F32 reinterpret() {
        return F32.decode(bits());
    }

    public int getVal() {
        return val;
    }

    public long getValU() {
        return Integer.toUnsignedLong(val);
    }

    @Override
    public String toString() {
        return "I32{" +
                "val=" + val +
                '}';
    }

    private I32 bool(final boolean bool) {
        return bool ? I32.ONE : I32.ZERO;
    }
}
