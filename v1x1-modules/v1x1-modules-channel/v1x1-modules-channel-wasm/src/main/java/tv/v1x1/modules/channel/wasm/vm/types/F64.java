package tv.v1x1.modules.channel.wasm.vm.types;

import com.google.common.primitives.Longs;

import java.math.BigDecimal;

public final class F64 extends FN {
    public static F64 decode(final byte[] bits) {
        return new F64(Double.longBitsToDouble(Longs.fromByteArray(bits)));
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
        return null;
    }

    @Override
    public I64 eq(final FN other) {
        return null;
    }

    @Override
    public I64 ne(final FN other) {
        return null;
    }

    @Override
    public I64 lt(final FN other) {
        return null;
    }

    @Override
    public I64 gt(final FN other) {
        return null;
    }

    @Override
    public I64 le(final FN other) {
        return null;
    }

    @Override
    public I64 ge(final FN other) {
        return null;
    }

    @Override
    public I64 truncU() {
        return null;
    }

    @Override
    public I64 truncS() {
        return null;
    }

    @Override
    public F64 promote() {
        return null;
    }

    @Override
    public F32 demote() {
        return null;
    }

    @Override
    public I64 reinterpret() {
        return null;
    }
}
