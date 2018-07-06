package tv.v1x1.modules.channel.wasm.vm;

import java.util.List;
import java.util.Optional;

public class Context {
    private final List<FunctionType> types;
    private final List<FunctionType> funcs;
    private final List<TableType> tables;
    private final List<MemoryType> memories;
    private final List<GlobalType> globals;
    private final List<ValType> locals;
    private final List<ResultType> labels;
    private final Optional<ResultType> retType;

    public Context(final List<FunctionType> types, final List<FunctionType> funcs, final List<TableType> tables,
                   final List<MemoryType> memories, final List<GlobalType> globals, final List<ValType> locals,
                   final List<ResultType> labels, final Optional<ResultType> retType) {
        this.types = types;
        this.funcs = funcs;
        this.tables = tables;
        this.memories = memories;
        this.globals = globals;
        this.locals = locals;
        this.labels = labels;
        this.retType = retType;
    }

    public List<MemoryType> getMemories() {
        return memories;
    }

    public List<GlobalType> getGlobals() {
        return globals;
    }

    public List<ValType> getLocals() {
        return locals;
    }
}
