package tv.v1x1.modules.channel.wasm.vm.store;

import java.util.List;

public class WebAssemblyStore {
    private final List<FunctionInstance> functions;
    private final List<TableInstance> tables;
    private final List<MemoryInstance> memories;
    private final List<GlobalInstance> globals;

    public WebAssemblyStore(final List<FunctionInstance> functions, final List<TableInstance> tables, final List<MemoryInstance> memories, final List<GlobalInstance> globals) {
        this.functions = functions;
        this.tables = tables;
        this.memories = memories;
        this.globals = globals;
    }

    public List<FunctionInstance> getFunctions() {
        return functions;
    }

    public List<TableInstance> getTables() {
        return tables;
    }

    public List<MemoryInstance> getMemories() {
        return memories;
    }

    public List<GlobalInstance> getGlobals() {
        return globals;
    }
}
