package tv.v1x1.modules.channel.wasm.vm.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.decoder.MemoryType;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryInstance {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final int PAGE_SIZE = 65536; // 64KiB
    public static final int MAX_SIZE = 2000; // 2000 * 64KiB = 128MiB
    private final Map<Integer, MemoryPage> pages = new HashMap<>();
    private Optional<I32> max;
    private int currentBreak = 0;
    private int breakPages = 0;

    public MemoryInstance(final Optional<I32> max) {
        this.max = max;
    }

    public int grow(final int val) {
        final int oldPageCount = breakPages;
        final int newPageCount = oldPageCount + val;
        if(max.isPresent() && newPageCount > max.get().getValU())
            return -1;
        LOG.info("Growing VM memory by {}", val);
        for(int pageAddress = oldPageCount; pageAddress < newPageCount; pageAddress++, breakPages++)
            if(mapAt(pageAddress, new MemoryPage(true, true)) < 0)
                return -1;
        return oldPageCount;
    }

    public int forceMapAt(final int pageAddress, final MemoryPage ...memoryPages) {
        for(int i = 0; i < memoryPages.length; i++)
            if(pages.containsKey(pageAddress + i))
                unmap(pageAddress + i);
        return mapAt(pageAddress, memoryPages);
    }

    public int mapAt(final int pageAddress, final MemoryPage ...memoryPages) {
        if(getPageCount() + memoryPages.length > MAX_SIZE)
            return -1;
        if(!canMapPagesAt(pageAddress, memoryPages.length))
            return -2;
        for(int i = 0; i < memoryPages.length; i++)
            pages.put(pageAddress + i, memoryPages[i]);
        return pageAddress;
    }

    public int map(final MemoryPage ...memoryPages) {
        for(int pageAddress = Short.MAX_VALUE; pageAddress > 0; pageAddress--) {
            final int address = mapAt(pageAddress, memoryPages);
            if(address == -2)
                continue;
            return address;
        }
        return -1;
    }

    public int tryMapAt(final int hint, final MemoryPage ...memoryPages) {
        for(int pageAddress = hint; pageAddress < Short.MAX_VALUE; pageAddress++) {
            final int address = mapAt(pageAddress, memoryPages);
            if(address == -2)
                continue;
            return address;
        }
        return -1;
    }

    public void unmap(final int pageAddress) {
        pages.remove(pageAddress);
    }

    public void unmap(final int pageAddress, final int count) {
        for(int i = 0; i < count; i++)
            unmap(pageAddress + i);
    }

    public int getPageCount() {
        return pages.size();
    }

    public int getCurrentBreak() {
        return currentBreak;
    }

    public void setCurrentBreak(final int currentBreak) {
        this.currentBreak = currentBreak;
    }

    public int getBreakPages() {
        return breakPages;
    }

    public void write(final int address, final byte[] bytes) throws TrapException {
        for(int page = address >> 16; page <= (address + bytes.length) >> 16; page++) {
            final MemoryPage memoryPage = pages.get(page);
            if(memoryPage == null)
                throw new TrapException("Segmentation fault: Page " + page + " not mapped");
            if(!memoryPage.isWritable())
                throw new TrapException("Segmentation fault: Page " + page + " not writable");
            System.arraycopy(
                    bytes, Math.max(0, (page << 16) - address),
                    memoryPage.getData(), Math.max(address, page << 16) - (page << 16),
                    Math.min(PAGE_SIZE, bytes.length - Math.max(0, (page << 16) - address)));
        }
    }

    public byte readByte(final int address) throws TrapException {
        final int page = address >> 16;
        final MemoryPage memoryPage = pages.get(page);
        if(memoryPage == null)
            throw new TrapException("Segmentation fault: Page " + page + " not mapped");
        if(!memoryPage.isReadable())
            throw new TrapException("Segmentation fault: Page " + page + " not readable");
        return memoryPage.getData()[address & 0xffff];
    }

    public void read(final int address, final byte[] bytes) throws TrapException {
        for(int page = address >> 16; page <= (address + bytes.length) >> 16; page++) {
            final MemoryPage memoryPage = pages.get(page);
            if(memoryPage == null)
                throw new TrapException("Segmentation fault: Page " + page + " not mapped");
            if(!memoryPage.isReadable())
                throw new TrapException("Segmentation fault: Page " + page + " not readable");
            System.arraycopy(
                    memoryPage.getData(), Math.max(address, page << 16) - (page << 16),
                    bytes, Math.max(0, (page << 16) - address),
                    Math.min(PAGE_SIZE, bytes.length - Math.max(0, (page << 16) - address)));
        }
    }

    public boolean matches(final MemoryType memoryType) {
        return getBreakPages() >= memoryType.getLimits().getMin().getValU()
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

    private boolean canMapPagesAt(int pageAddress, int count) {
        for(int i = pageAddress; i < pageAddress + count; i++)
            if(pages.containsKey(i))
                return false;
        return true;
    }
}
