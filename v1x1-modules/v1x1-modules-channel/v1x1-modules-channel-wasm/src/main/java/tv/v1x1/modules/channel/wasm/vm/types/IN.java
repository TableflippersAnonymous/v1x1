package tv.v1x1.modules.channel.wasm.vm.types;

public abstract class IN extends WebAssemblyType {
    public abstract IN add(final IN other);
    public abstract IN sub(final IN other);
    public abstract IN mul(final IN other);
    public abstract IN divU(final IN other);
    public abstract IN divS(final IN other);
    public abstract IN remU(final IN other);
    public abstract IN remS(final IN other);
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
    public abstract IN eqz();
    public abstract IN eq(final IN other);
    public abstract IN ne(final IN other);
    public abstract IN ltU(final IN other);
    public abstract IN ltS(final IN other);
    public abstract IN gtU(final IN other);
    public abstract IN gtS(final IN other);
    public abstract IN leU(final IN other);
    public abstract IN leS(final IN other);
    public abstract IN geU(final IN other);
    public abstract IN geS(final IN other);
    public abstract I64 extendU();
    public abstract I64 extendS();
    public abstract I32 wrap();
    public abstract FN reinterpret();
}
