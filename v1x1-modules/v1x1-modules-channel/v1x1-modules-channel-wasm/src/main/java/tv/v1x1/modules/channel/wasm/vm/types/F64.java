package tv.v1x1.modules.channel.wasm.vm.types;

import com.google.common.primitives.Longs;
import tv.v1x1.modules.channel.wasm.vm.TrapException;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;

public final class F64 extends FN {
    public static final F64 ZERO = new F64(0);

    public static F64 decode(final byte[] bits) {
        return new F64(Double.longBitsToDouble(Longs.fromByteArray(bits)));
    }

    public static F64 decode(final DataInputStream input) throws IOException {
        final byte[] bits = new byte[8];
        input.readFully(bits);
        return decode(swapEndian(bits));
    }

    private final double val;

    public F64(final double val) {
        this.val = val;
    }

    @Override
    public byte[] bits() {
        return Longs.toByteArray(Double.doubleToRawLongBits(val));
    }

    @Override
    public F64 add(final FN other) {
        return new F64(val + other.promote().val);
    }

    @Override
    public F64 sub(final FN other) {
        return new F64(val - other.promote().val);
    }

    @Override
    public F64 mul(final FN other) {
        return new F64(val * other.promote().val);
    }

    @Override
    public F64 div(final FN other) {
        return new F64(val / other.promote().val);
    }

    @Override
    public F64 min(final FN other) {
        return new F64(Double.min(val, other.promote().val));
    }

    @Override
    public F64 max(final FN other) {
        return new F64(Double.max(val, other.promote().val));
    }

    @Override
    public F64 copysign(final FN other) {
        return new F64(Math.copySign(val, other.promote().val));
    }

    @Override
    public F64 abs() {
        return new F64(Math.abs(val));
    }

    @Override
    public F64 neg() {
        return new F64(-val);
    }

    @Override
    public F64 sqrt() {
        return new F64(Math.sqrt(val));
    }

    @Override
    public F64 ceil() {
        return new F64(Math.ceil(val));
    }

    @Override
    public F64 floor() {
        return new F64(Math.floor(val));
    }

    @Override
    public F64 trunc() {
        return new F64(BigDecimal.valueOf(val).setScale(0, BigDecimal.ROUND_DOWN).doubleValue());
    }

    @Override
    public F64 nearest() {
        return new F64(Math.round(val));
    }

    @Override
    public I32 eq(final FN other) {
        return bool(val == other.promote().val);
    }

    @Override
    public I32 ne(final FN other) {
        return bool(val != other.promote().val);
    }

    @Override
    public I32 lt(final FN other) {
        return bool(val < other.promote().val);
    }

    @Override
    public I32 gt(final FN other) {
        return bool(val > other.promote().val);
    }

    @Override
    public I32 le(final FN other) {
        return bool(val <= other.promote().val);
    }

    @Override
    public I32 ge(final FN other) {
        return bool(val >= other.promote().val);
    }

    @Override
    public I32 truncUI32() throws TrapException {
        if(Double.isNaN(val) || Double.isInfinite(val))
            throw new TrapException();
        final F64 trunc = trunc();
        if(trunc.val >= 4294967296F || trunc.val < 0F)
            throw new TrapException();
        return new I32(BigDecimal.valueOf(trunc.val).intValue());
    }

    @Override
    public I32 truncSI32() throws TrapException {
        if(Double.isNaN(val) || Double.isInfinite(val))
            throw new TrapException();
        final F64 trunc = trunc();
        if(trunc.val > Integer.MAX_VALUE || trunc.val < Integer.MIN_VALUE)
            throw new TrapException();
        return new I32((int) trunc.val);
    }

    @Override
    public I64 truncUI64() throws TrapException {
        if(Double.isNaN(val) || Double.isInfinite(val))
            throw new TrapException();
        final F64 trunc = trunc();
        if(trunc.val >= 18446744073709551616D || trunc.val < 0D)
            throw new TrapException();
        return new I64(BigDecimal.valueOf(trunc.val).longValue());
    }

    @Override
    public I64 truncSI64() throws TrapException {
        if(Double.isNaN(val) || Double.isInfinite(val))
            throw new TrapException();
        final F64 trunc = trunc();
        if(trunc.val > Long.MAX_VALUE || trunc.val < Long.MIN_VALUE)
            throw new TrapException();
        return new I64((long) trunc.val);
    }

    @Override
    public F64 promote() {
        return this;
    }

    @Override
    public F32 demote() {
        return new F32((float) val);
    }

    @Override
    public I64 reinterpret() {
        return I64.decode(bits());
    }

    private I32 bool(final boolean bool) {
        return bool ? I32.ONE : I32.ZERO;
    }
}
