package tv.v1x1.modules.channel.wasm.vm.types;

public abstract class FN extends WebAssemblyType {
    public abstract FN add(final FN other);
    public abstract FN sub(final FN other);
    public abstract FN mul(final FN other);
    public abstract FN div(final FN other);
    public abstract FN min(final FN other);
    public abstract FN max(final FN other);
    public abstract FN copysign(final FN other);
    public abstract FN abs();
    public abstract FN neg();
    public abstract FN sqrt();
    public abstract FN ceil();
    public abstract FN floor();
    public abstract FN trunc();
    public abstract FN nearest();
    public abstract IN eq(final FN other);
    public abstract IN ne(final FN other);
    public abstract IN lt(final FN other);
    public abstract IN gt(final FN other);
    public abstract IN le(final FN other);
    public abstract IN ge(final FN other);
    public abstract IN truncU();
    public abstract IN truncS();
    public abstract F64 promote();
    public abstract F32 demote();
    public abstract IN reinterpret();
}
