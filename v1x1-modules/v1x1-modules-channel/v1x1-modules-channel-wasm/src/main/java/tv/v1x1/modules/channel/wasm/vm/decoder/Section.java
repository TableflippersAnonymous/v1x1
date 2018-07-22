package tv.v1x1.modules.channel.wasm.vm.decoder;

public class Section {
    private final byte id;
    private final byte[] bytes;

    public Section(final byte id, final byte[] bytes) {
        this.id = id;
        this.bytes = bytes;
    }

    public byte getId() {
        return id;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
