package tv.v1x1.modules.channel.wasm.vm.types;

import com.google.common.primitives.Ints;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;

public final class F32 extends FN {
    public static final F32 ZERO = new F32(0);

    public static F32 decode(final byte[] bits) {
        return new F32(Float.intBitsToFloat(Ints.fromByteArray(bits)));
    }

    public static F32 decode(final DataInputStream input) throws IOException {
        final byte[] bits = new byte[4];
        input.readFully(bits);
        return decode(swapEndian(bits));
    }

    private final float val;

    public F32(final float val) {
        this.val = val;
    }

    @Override
    public byte[] bits() {
        return Ints.toByteArray(Float.floatToRawIntBits(val));
    }

    @Override
    public F32 add(final FN other) {
        return new F32(val + other.demote().val);
    }

    @Override
    public F32 sub(final FN other) {
        return new F32(val - other.demote().val);
    }

    @Override
    public F32 mul(final FN other) {
        return new F32(val * other.demote().val);
    }

    @Override
    public F32 div(final FN other) {
        return new F32(val / other.demote().val);
    }

    @Override
    public F32 min(final FN other) {
        return new F32(Float.min(val, other.demote().val));
    }

    @Override
    public F32 max(final FN other) {
        return new F32(Float.max(val, other.demote().val));
    }

    @Override
    public F32 copysign(final FN other) {
        return new F32(Math.copySign(val, other.demote().val));
    }

    @Override
    public F32 abs() {
        return new F32(Math.abs(val));
    }

    @Override
    public F32 neg() {
        return new F32(-val);
    }

    @Override
    public F32 sqrt() {
        return new F32((float) Math.sqrt(val));
    }

    @Override
    public F32 ceil() {
        return new F32((float) Math.ceil(val));
    }

    @Override
    public F32 floor() {
        return new F32((float) Math.floor(val));
    }

    @Override
    public F32 trunc() {
        return new F32(BigDecimal.valueOf(val).setScale(0, BigDecimal.ROUND_DOWN).floatValue());
    }

    @Override
    public F32 nearest() {
        return new F32(Math.round(val));
    }

    @Override
    public I32 eq(final FN other) {
        return bool(val == other.demote().val);
    }

    @Override
    public I32 ne(final FN other) {
        return bool(val != other.demote().val);
    }

    @Override
    public I32 lt(final FN other) {
        return bool(val < other.demote().val);
    }

    @Override
    public I32 gt(final FN other) {
        return bool(val > other.demote().val);
    }

    @Override
    public I32 le(final FN other) {
        return bool(val <= other.demote().val);
    }

    @Override
    public I32 ge(final FN other) {
        return bool(val >= other.demote().val);
    }

    @Override
    public I32 truncUI32() throws TrapException {
        if(Float.isNaN(val) || Float.isInfinite(val))
            throw new TrapException("Cannot trunc NaN/INF");
        final F32 trunc = trunc();
        if(trunc.val >= 4294967296F || trunc.val < 0F)
            throw new TrapException("Value too large to trunc");
        return new I32(BigDecimal.valueOf(trunc.val).intValue());
    }

    @Override
    public I32 truncSI32() throws TrapException {
        if(Float.isNaN(val) || Float.isInfinite(val))
            throw new TrapException("Cannot trunc NaN/INF");
        final F32 trunc = trunc();
        if(trunc.val > Integer.MAX_VALUE || trunc.val < Integer.MIN_VALUE)
            throw new TrapException("Value too large to trunc");
        return new I32((int) trunc.val);
    }

    @Override
    public I64 truncUI64() throws TrapException {
        if(Float.isNaN(val) || Float.isInfinite(val))
            throw new TrapException("Cannot trunc NaN/INF");
        final F32 trunc = trunc();
        if(trunc.val >= 18446744073709551616D || trunc.val < 0D)
            throw new TrapException("Value too large to trunc");
        return new I64(BigDecimal.valueOf(trunc.val).longValue());
    }

    @Override
    public I64 truncSI64() throws TrapException {
        if(Float.isNaN(val) || Float.isInfinite(val))
            throw new TrapException("Cannot trunc NaN/INF");
        final F32 trunc = trunc();
        if(trunc.val > Long.MAX_VALUE || trunc.val < Long.MIN_VALUE)
            throw new TrapException("Value too large to trunc");
        return new I64((long) trunc.val);
    }

    @Override
    public F64 promote() {
        return new F64((double) val);
    }

    @Override
    public F32 demote() {
        return this;
    }

    @Override
    public I32 reinterpret() {
        return I32.decode(bits());
    }

    @Override
    public String toString() {
        return "F32{" +
                "val=" + val +
                '}';
    }

    private I32 bool(final boolean bool) {
        return bool ? I32.ONE : I32.ZERO;
    }
}
