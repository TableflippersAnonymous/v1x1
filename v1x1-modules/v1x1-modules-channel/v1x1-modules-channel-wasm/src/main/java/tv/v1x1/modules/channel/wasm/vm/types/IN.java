package tv.v1x1.modules.channel.wasm.vm.types;

import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;

public abstract class IN extends WebAssemblyType {
    public abstract IN add(final IN other);
    public abstract IN sub(final IN other);
    public abstract IN mul(final IN other);
    public abstract IN divU(final IN other) throws TrapException;
    public abstract IN divS(final IN other) throws TrapException;
    public abstract IN remU(final IN other) throws TrapException;
    public abstract IN remS(final IN other) throws TrapException;
    public abstract IN and(final IN other);
    public abstract IN or(final IN other);
    public abstract IN xor(final IN other);
    public abstract IN shl(final IN other);
    public abstract IN shrU(final IN other);
    public abstract IN shrS(final IN other);
    public abstract IN rotl(final IN other);
    public abstract IN rotr(final IN other);
    public abstract IN clz();
    public abstract IN ctz();
    public abstract IN popcnt();
    public abstract I32 eqz();
    public abstract I32 eq(final IN other);
    public abstract I32 ne(final IN other);
    public abstract I32 ltU(final IN other);
    public abstract I32 ltS(final IN other);
    public abstract I32 gtU(final IN other);
    public abstract I32 gtS(final IN other);
    public abstract I32 leU(final IN other);
    public abstract I32 leS(final IN other);
    public abstract I32 geU(final IN other);
    public abstract I32 geS(final IN other);
    public abstract I64 extendU();
    public abstract I64 extendS();
    public abstract I32 wrap();
    public abstract F32 convertUF32();
    public abstract F32 convertSF32();
    public abstract F64 convertUF64();
    public abstract F64 convertSF64();
    public abstract FN reinterpret();
}
