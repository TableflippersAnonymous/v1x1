package tv.v1x1.modules.channel.wasm.vm;

import java.util.List;

public class WebAssemblyModule {
    private List<FunctionType> functionTypes;
    private List<FunctionDef> functions;
    private List<TableDef> tables;
    private List<MemoryDef> memories;
    private List<GlobalDef> globals;
    private List<ElementSegmentDef> elementSegments;
    private List<DataSegmentDef> dataSegments;
    private StartDef start;
    private List<ImportDef> imports;
    private List<ExportDef> exports;
}
