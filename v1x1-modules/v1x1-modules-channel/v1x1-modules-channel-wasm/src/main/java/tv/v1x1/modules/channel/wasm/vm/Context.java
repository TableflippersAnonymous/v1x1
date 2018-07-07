package tv.v1x1.modules.channel.wasm.vm;

import java.util.ArrayList;
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

    public List<FunctionType> getTypes() {
        return types;
    }

    public List<FunctionType> getFuncs() {
        return funcs;
    }

    public List<TableType> getTables() {
        return tables;
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

    public List<ResultType> getLabels() {
        return labels;
    }

    public Optional<ResultType> getRetType() {
        return retType;
    }

    public Context addLabel(final ResultType resultType) {
        final List<ResultType> newLabels = new ArrayList<>();
        newLabels.add(resultType);
        newLabels.addAll(labels);
        return new Context(types, funcs, tables, memories, globals, locals, newLabels, retType);
    }
}
