package tv.v1x1.modules.channel.wasm.vm;

import java.util.List;
import java.util.Optional;

public class Context {
    private List<FunctionType> types;
    private List<FunctionType> funcs;
    private List<TableType> tables;
    private List<MemoryType> mems;
    private List<GlobalType> globals;
    private List<ValType> locals;
    private List<ResultType> labels;
    private Optional<ResultType> retType;
}
