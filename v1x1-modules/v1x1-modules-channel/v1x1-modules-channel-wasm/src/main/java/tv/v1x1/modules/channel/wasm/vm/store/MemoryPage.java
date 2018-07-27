package tv.v1x1.modules.channel.wasm.vm.store;

public class MemoryPage {
    private final byte[] data = new byte[65536];
    private final boolean readable;
    private final boolean writable;

    public MemoryPage(final boolean readable, final boolean writable) {
        this.readable = readable;
        this.writable = writable;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isReadable() {
        return readable;
    }

    public boolean isWritable() {
        return writable;
    }
}
