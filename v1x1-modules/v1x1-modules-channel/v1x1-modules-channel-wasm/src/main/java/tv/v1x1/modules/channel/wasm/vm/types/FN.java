package tv.v1x1.modules.channel.wasm.vm.types;

import tv.v1x1.modules.channel.wasm.vm.TrapException;

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
    public abstract I32 eq(final FN other);
    public abstract I32 ne(final FN other);
    public abstract I32 lt(final FN other);
    public abstract I32 gt(final FN other);
    public abstract I32 le(final FN other);
    public abstract I32 ge(final FN other);
    public abstract I32 truncUI32() throws TrapException;
    public abstract I32 truncSI32() throws TrapException;
    public abstract I64 truncUI64() throws TrapException;
    public abstract I64 truncSI64() throws TrapException;
    public abstract F64 promote();
    public abstract F32 demote();
    public abstract IN reinterpret();
}
