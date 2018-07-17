package tv.v1x1.modules.channel.wasm.vm.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.modules.channel.wasm.vm.decoder.MemoryType;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

public class MemoryInstance {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final int PAGE_SIZE = 65536; // 64KiB
    public static final int MAX_SIZE = 2000; // 2000 * 64KiB = 128MiB
    private byte[] data;
    private Optional<I32> max;
    private int currentPosition;

    public MemoryInstance(final byte[] data, final Optional<I32> max) {
        this.data = data;
        this.max = max;
        this.currentPosition = data.length;
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
        LOG.info("Growing VM memory by {}", val);
        final byte[] oldData = data;
        data = new byte[newPageCount * PAGE_SIZE];
        System.arraycopy(oldData, 0, data, 0, Math.min(data.length, oldData.length));
        return oldPageCount;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(final int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public boolean matches(final MemoryType memoryType) {
        return data.length / PAGE_SIZE >= memoryType.getLimits().getMin().getValU()
                && (!memoryType.getLimits().getMax().isPresent()
                || (max.isPresent() && max.get().getValU() <= memoryType.getLimits().getMax().get().getValU()));
    }

    @Override
    public String toString() {
        return "MemoryInstance{" +
                "data=[SNIP]" +
                ", max=" + max +
                '}';
    }
}
