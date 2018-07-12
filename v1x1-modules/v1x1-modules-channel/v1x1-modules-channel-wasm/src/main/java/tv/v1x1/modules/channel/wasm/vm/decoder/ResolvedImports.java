package tv.v1x1.modules.channel.wasm.vm.decoder;

public class ResolvedImports {
    private final int[] functionAddresses;
    private final int[] tableAddresses;
    private final int[] memoryAddresses;
    private final int[] globalAddresses;

    public ResolvedImports(final int[] functionAddresses, final int[] tableAddresses, final int[] memoryAddresses, final int[] globalAddresses) {
        this.functionAddresses = functionAddresses;
        this.tableAddresses = tableAddresses;
        this.memoryAddresses = memoryAddresses;
        this.globalAddresses = globalAddresses;
    }

    public int[] getFunctionAddresses() {
        return functionAddresses;
    }

    public int[] getTableAddresses() {
        return tableAddresses;
    }

    public int[] getMemoryAddresses() {
        return memoryAddresses;
    }

    public int[] getGlobalAddresses() {
        return globalAddresses;
    }
}
