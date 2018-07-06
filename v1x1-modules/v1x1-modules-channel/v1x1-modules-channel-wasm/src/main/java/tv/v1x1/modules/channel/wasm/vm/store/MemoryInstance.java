package tv.v1x1.modules.channel.wasm.vm.store;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.util.Optional;

public class MemoryInstance {
    private byte[] data;
    private Optional<I32> max;

    public MemoryInstance(final byte[] data, final Optional<I32> max) {
        this.data = data;
        this.max = max;
    }

    public byte[] getData() {
        return data;
    }
}
