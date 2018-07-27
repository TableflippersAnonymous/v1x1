package tv.v1x1.modules.channel.wasm.vm.runtime;

import tv.v1x1.modules.channel.wasm.vm.decoder.ExportDef;
import tv.v1x1.modules.channel.wasm.vm.validation.FunctionType;

import java.util.Arrays;

public class ModuleInstance {
    private final FunctionType[] types;
    private final int[] functionAddresses;
    private final int[] tableAddresses;
    private final int[] memoryAddresses;
    private final int[] globalAddresses;
    private final ExportDef[] exports;

    public ModuleInstance(final FunctionType[] types, final int[] functionAddresses, final int[] tableAddresses,
                          final int[] memoryAddresses, final int[] globalAddresses, final ExportDef[] exports) {
        this.types = types;
        this.functionAddresses = functionAddresses;
        this.tableAddresses = tableAddresses;
        this.memoryAddresses = memoryAddresses;
        this.globalAddresses = globalAddresses;
        this.exports = exports;
    }

    public FunctionType[] getTypes() {
        return types;
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

    public ExportDef[] getExports() {
        return exports;
    }

    @Override
    public String toString() {
        return "ModuleInstance{" +
                "types=" + Arrays.toString(types) +
                ", functionAddresses=" + Arrays.toString(functionAddresses) +
                ", tableAddresses=" + Arrays.toString(tableAddresses) +
                ", memoryAddresses=" + Arrays.toString(memoryAddresses) +
                ", globalAddresses=" + Arrays.toString(globalAddresses) +
                ", exports=" + Arrays.toString(exports) +
                '}';
    }
}
