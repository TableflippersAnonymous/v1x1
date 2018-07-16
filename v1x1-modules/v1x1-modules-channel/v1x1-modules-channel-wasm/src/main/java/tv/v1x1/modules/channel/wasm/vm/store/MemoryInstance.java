package tv.v1x1.modules.channel.wasm.vm.store;

import tv.v1x1.modules.channel.wasm.vm.decoder.MemoryType;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.util.Arrays;
import java.util.Optional;

public class MemoryInstance {
    public static final int PAGE_SIZE = 65536; // 64KiB
    public static final int MAX_SIZE = 2000; // 2000 * 64KiB = 128MiB
    private byte[] data;
    private Optional<I32> max;

    public MemoryInstance(final byte[] data, final Optional<I32> max) {
        this.data = data;
        this.max = max;
    }

    public byte[] getData() {
        return data;
    }

    public int grow(final int val) {
        final int oldPageCount = data.length / PAGE_SIZE;
        final int newPageCount = oldPageCount + val;
        if(max.isPresent() && newPageCount > max.get().getValU())
            return -1;
        if(newPageCount > MAX_SIZE)
            return -1;
        final byte[] oldData = data;
        data = new byte[newPageCount * PAGE_SIZE];
        System.arraycopy(oldData, 0, data, 0, Math.min(data.length, oldData.length));
        return oldPageCount;
    }

    public boolean matches(final MemoryType memoryType) {
        return data.length / PAGE_SIZE >= memoryType.getLimits().getMin().getValU()
                && (!memoryType.getLimits().getMax().isPresent()
                || (max.isPresent() && max.get().getValU() <= memoryType.getLimits().getMax().get().getValU()));
    }

    @Override
    public String toString() {
        return "MemoryInstance{" +
                "data=" + Arrays.toString(data) +
                ", max=" + max +
                '}';
    }
}
