package tv.v1x1.modules.channel.wasm.vm.decoder;

import com.google.common.collect.ImmutableList;
import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.DecodeException;
import tv.v1x1.modules.channel.wasm.vm.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.GlobalType;
import tv.v1x1.modules.channel.wasm.vm.ModuleInstance;
import tv.v1x1.modules.channel.wasm.vm.Mutable;
import tv.v1x1.modules.channel.wasm.vm.Section;
import tv.v1x1.modules.channel.wasm.vm.TableType;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.stack.Activation;
import tv.v1x1.modules.channel.wasm.vm.store.FunctionInstance;
import tv.v1x1.modules.channel.wasm.vm.store.GlobalInstance;
import tv.v1x1.modules.channel.wasm.vm.store.LinkingException;
import tv.v1x1.modules.channel.wasm.vm.store.MemoryInstance;
import tv.v1x1.modules.channel.wasm.vm.store.TableInstance;
import tv.v1x1.modules.channel.wasm.vm.store.WebAssemblyFunctionInstance;
import tv.v1x1.modules.channel.wasm.vm.store.WebAssemblyStore;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModuleDef {
    private static final byte[] MAGIC = { 0x00, 0x61, 0x73, 0x6d };
    private static final byte[] VERSION = { 0x01, 0x00, 0x00, 0x00 };

    private final List<FunctionType> functionTypes;
    private final List<FunctionDef> functions;
    private final List<TableDef> tables;
    private final List<MemoryDef> memories;
    private final List<GlobalDef> globals;
    private final List<ElementSegmentDef> elementSegments;
    private final List<DataSegmentDef> dataSegments;
    private final Optional<StartDef> start;
    private final List<ImportDef> imports;
    private final List<ExportDef> exports;

    public ModuleDef(final List<FunctionType> functionTypes, final List<FunctionDef> functions,
                     final List<TableDef> tables, final List<MemoryDef> memories, final List<GlobalDef> globals,
                     final List<ElementSegmentDef> elementSegments, final List<DataSegmentDef> dataSegments,
                     final Optional<StartDef> start, final List<ImportDef> imports, final List<ExportDef> exports) {
        this.functionTypes = functionTypes;
        this.functions = functions;
        this.tables = tables;
        this.memories = memories;
        this.globals = globals;
        this.elementSegments = elementSegments;
        this.dataSegments = dataSegments;
        this.start = start;
        this.imports = imports;
        this.exports = exports;
    }

    public static ModuleDef decode(final DataInputStream dataInputStream) throws IOException {
        final List<FunctionType> functionTypes = new ArrayList<>();
        final List<FunctionDef> functions = new ArrayList<>();
        final List<Integer> types = new ArrayList<>();
        final List<TableDef> tables = new ArrayList<>();
        final List<MemoryDef> memories = new ArrayList<>();
        final List<GlobalDef> globals = new ArrayList<>();
        final List<ElementSegmentDef> elementSegments = new ArrayList<>();
        final List<DataSegmentDef> dataSegments = new ArrayList<>();
        Optional<StartDef> start = Optional.empty();
        final List<ImportDef> imports = new ArrayList<>();
        final List<ExportDef> exports = new ArrayList<>();

        decodeMagic(dataInputStream);
        decodeVersion(dataInputStream);
        for(;;) {
            final Section section = decodeSection(dataInputStream);
            if(section == null)
                break;
            final DataInputStream sectionInputStream = new DataInputStream(new ByteArrayInputStream(section.getBytes()));
            switch(section.getId()) {
                case 0x00: /* Custom Section */
                    /* Ignore */
                    break;
                case 0x01: /* Type Section */
                    functionTypes.addAll(FunctionType.decodeVec(sectionInputStream));
                    break;
                case 0x02: /* Import Section */
                    imports.addAll(ImportDef.decodeVec(sectionInputStream));
                    break;
                case 0x03: /* Function Section */
                    types.addAll(decodeU32Vec(sectionInputStream));
                    break;
                case 0x04: /* Table Section */
                    tables.addAll(TableDef.decodeVec(sectionInputStream));
                    break;
                case 0x05: /* Memory Section */
                    memories.addAll(MemoryDef.decodeVec(sectionInputStream));
                    break;
                case 0x06: /* Global Section */
                    globals.addAll(GlobalDef.decodeVec(sectionInputStream));
                    break;
                case 0x07: /* Export Section */
                    exports.addAll(ExportDef.decodeVec(sectionInputStream));
                    break;
                case 0x08: /* Start Section */
                    start = Optional.of(StartDef.decode(sectionInputStream));
                    break;
                case 0x09: /* Element Section */
                    elementSegments.addAll(ElementSegmentDef.decodeVec(sectionInputStream));
                    break;
                case 0x0a: /* Code Section */
                    functions.addAll(FunctionDef.decodeVec(sectionInputStream, types));
                    break;
                case 0x0b: /* Data Section */
                    dataSegments.addAll(DataSegmentDef.decodeVec(sectionInputStream));
                    break;
                default:
                    throw new DecodeException("Invalid section");
            }
        }

        return new ModuleDef(
                ImmutableList.copyOf(functionTypes),
                ImmutableList.copyOf(functions),
                ImmutableList.copyOf(tables),
                ImmutableList.copyOf(memories),
                ImmutableList.copyOf(globals),
                ImmutableList.copyOf(elementSegments),
                ImmutableList.copyOf(dataSegments),
                start,
                ImmutableList.copyOf(imports),
                ImmutableList.copyOf(exports)
        );
    }

    private static void decodeMagic(final DataInputStream dataInputStream) throws IOException {
        final byte[] magic = new byte[4];
        dataInputStream.readFully(magic);
        if(!Arrays.equals(magic, MAGIC))
            throw new DecodeException("Bad magic");
    }

    private static void decodeVersion(final DataInputStream dataInputStream) throws IOException {
        final byte[] version = new byte[4];
        dataInputStream.readFully(version);
        if(!Arrays.equals(version, VERSION))
            throw new DecodeException("Bad version");
    }

    private static Section decodeSection(final DataInputStream dataInputStream) throws IOException {
        final byte id;
        try {
            id = dataInputStream.readByte();
        } catch(final EOFException e) {
            return null;
        }
        final I32 size = I32.decodeU(dataInputStream);
        final byte[] bytes = new byte[(int) size.getValU()];
        dataInputStream.readFully(bytes);
        return new Section(id, bytes);
    }

    public static String decodeString(final DataInputStream dataInputStream) throws IOException {
        return new String(decodeByteVec(dataInputStream), Charset.forName("UTF-8"));
    }

    public static byte[] decodeByteVec(final DataInputStream dataInputStream) throws IOException {
        final long count = I32.decodeU(dataInputStream).getValU();
        final byte[] bytes = new byte[(int) count];
        dataInputStream.readFully(bytes);
        return bytes;
    }

    public static List<Integer> decodeU32Vec(final DataInputStream dataInputStream) throws IOException {
        final long count = I32.decodeU(dataInputStream).getValU();
        final List<Integer> integers = new ArrayList<>((int) count);
        for(long i = 0; i < count; i++)
            integers.add((int) I32.decodeU(dataInputStream).getValU());
        return integers;
    }

    public void validate() throws ValidationException {
        final List<FunctionType> functions = getContextFunctions();
        final List<TableType> tableTypes = getContextTables();
        final List<MemoryType> memoryTypes = getContextMemories();
        final List<GlobalType> globalTypes = getContextGlobals();
        final Context context = new Context(ImmutableList.copyOf(functionTypes), ImmutableList.copyOf(functions),
                ImmutableList.copyOf(tableTypes), ImmutableList.copyOf(memoryTypes), ImmutableList.copyOf(globalTypes),
                ImmutableList.of(), ImmutableList.of(), Optional.empty());
        final Context globalImportContext = new Context(ImmutableList.of(), ImmutableList.of(), ImmutableList.of(),
                ImmutableList.of(), ImmutableList.copyOf(globals()), ImmutableList.of(), ImmutableList.of(),
                Optional.empty());
        for(final FunctionType functionType : functionTypes)
            functionType.validate();
        for(final FunctionDef functionDef : this.functions)
            functionDef.validate(context);
        for(final TableDef tableDef : tables)
            tableDef.validate();
        for(final MemoryDef memoryDef : memories)
            memoryDef.validate();
        for(final GlobalDef globalDef : globals)
            globalDef.validate(globalImportContext);
        for(final ElementSegmentDef elementSegmentDef : elementSegments)
            elementSegmentDef.validate(context);
        for(final DataSegmentDef dataSegmentDef : dataSegments)
            dataSegmentDef.validate(context);
        if(start.isPresent())
            start.get().validate(context);
        for(final ImportDef importDef : imports)
            importDef.validate(context);
        for(final ExportDef exportDef : exports)
            exportDef.validate(context);
        if(context.getTables().size() > 1)
            throw new ValidationException();
        if(context.getMemories().size() > 1)
            throw new ValidationException();
        final Set<String> exportNames = exports.stream().map(ExportDef::getName).collect(Collectors.toSet());
        if(exportNames.size() != exports.size())
            throw new ValidationException();
    }

    private List<GlobalType> globals() {
        return getImportDescriptorStream()
                .filter(importDescriptor -> importDescriptor instanceof GlobalImportDescriptor)
                .map(importDescriptor -> (GlobalImportDescriptor) importDescriptor)
                .map(GlobalImportDescriptor::getGlobalType)
                .collect(Collectors.toList());
    }

    private Stream<ImportDescriptor> getImportDescriptorStream() {
        return imports.stream()
                .map(ImportDef::getDescriptor);
    }

    private List<GlobalType> getContextGlobals() {
        return merge(globals(), globals.stream()
                .map(GlobalDef::getType)
                .collect(Collectors.toList()));
    }

    private List<MemoryType> getContextMemories() {
        return merge(getImportDescriptorStream()
                    .filter(importDescriptor -> importDescriptor instanceof MemImportDescriptor)
                    .map(importDescriptor -> (MemImportDescriptor) importDescriptor)
                    .map(MemImportDescriptor::getMemoryType)
                    .collect(Collectors.toList()),
                memories.stream()
                    .map(MemoryDef::getMemoryType)
                    .collect(Collectors.toList()));
    }

    private List<TableType> getContextTables() {
        return merge(getImportDescriptorStream()
                    .filter(importDescriptor -> importDescriptor instanceof TableImportDescriptor)
                    .map(importDescriptor -> (TableImportDescriptor) importDescriptor)
                    .map(TableImportDescriptor::getTableType)
                    .collect(Collectors.toList()),
                tables.stream()
                    .map(TableDef::getTableType)
                    .collect(Collectors.toList()));
    }

    private List<FunctionType> getContextFunctions() {
        return merge(getImportDescriptorStream()
                    .filter(importDescriptor -> importDescriptor instanceof FuncImportDescriptor)
                    .map(importDescriptor -> (FuncImportDescriptor) importDescriptor)
                    .map(FuncImportDescriptor::getTypeIdx)
                    .map(typeIdx -> functionTypes.get(typeIdx.intValue()))
                    .collect(Collectors.toList()),
                this.functions.stream()
                    .map(FunctionDef::getTypeIdx)
                    .map(functionTypes::get)
                    .collect(Collectors.toList()));
    }

    private <T> List<T> merge(final List<T> list1, final List<T> list2) {
        final List<T> list = new ArrayList<>(list1);
        list.addAll(list2);
        return list;
    }

    public ModuleInstance allocate(final WebAssemblyStore store) throws LinkingException {
        final FunctionType[] types = functionTypes.toArray(new FunctionType[] {});
        final int[] functionAddresses = new int[getContextFunctions().size()];
        final int[] tableAddresses = new int[getContextTables().size()];
        final int[] memoryAddresses = new int[getContextMemories().size()];
        final int[] globalAddresses = new int[getContextGlobals().size()];
        final ExportDef[] exports = new ExportDef[this.exports.size()];
        final ModuleInstance moduleInstance = new ModuleInstance(types, functionAddresses, tableAddresses,
                memoryAddresses, globalAddresses, exports);
        final ResolvedImports resolvedImports = store.resolveImports(types, imports);
        System.arraycopy(resolvedImports.getFunctionAddresses(), 0, functionAddresses, 0, resolvedImports.getFunctionAddresses().length);
        System.arraycopy(resolvedImports.getTableAddresses(), 0, tableAddresses, 0, resolvedImports.getTableAddresses().length);
        System.arraycopy(resolvedImports.getMemoryAddresses(), 0, memoryAddresses, 0, resolvedImports.getMemoryAddresses().length);
        System.arraycopy(resolvedImports.getGlobalAddresses(), 0, globalAddresses, 0, resolvedImports.getGlobalAddresses().length);
        for(int i = 0; i < functions.size(); i++) {
            final FunctionDef functionDef = functions.get(i);
            final FunctionInstance functionInstance = new WebAssemblyFunctionInstance(types[functionDef.getTypeIdx()],
                    functionDef.getLocals(), moduleInstance, functionDef.getBody());
            functionAddresses[resolvedImports.getFunctionAddresses().length + i] = store.allocateFunction(functionInstance);
        }
        for(int i = 0; i < tables.size(); i++) {
            final TableDef tableDef = tables.get(i);
            final List<Optional<Integer>> elements = new ArrayList<>();
            for(long j = 0; j < tableDef.getTableType().getLimits().getMin().getValU(); j++)
                elements.add(Optional.empty());
            final TableInstance tableInstance = new TableInstance(elements, tableDef.getTableType().getLimits().getMax().map(i32 -> (int) i32.getValU()));
            tableAddresses[resolvedImports.getTableAddresses().length + i] = store.allocateTable(tableInstance);
        }
        for(int i = 0; i < memories.size(); i++) {
            final MemoryDef memoryDef = memories.get(i);
            final MemoryInstance memoryInstance = new MemoryInstance(new byte[0], memoryDef.getMemoryType().getLimits().getMax());
            memoryInstance.grow((int) memoryDef.getMemoryType().getLimits().getMin().getValU());
            memoryAddresses[resolvedImports.getMemoryAddresses().length + i] = store.allocateMemory(memoryInstance);
        }
        for(int i = 0; i < globals.size(); i++) {
            final GlobalDef globalDef = globals.get(i);
            final GlobalInstance globalInstance = new GlobalInstance(globalDef.getType().getValType().getZero(), globalDef.getType().getMutable() == Mutable.VARIABLE);
            globalAddresses[resolvedImports.getGlobalAddresses().length + i] = store.allocateGlobal(globalInstance);
        }
        for(int i = 0; i < this.exports.size(); i++) {
            final ExportDef exportDef = this.exports.get(i);
            final ExportDescriptor exportDescriptor = exportDef.getDescriptor();
            final ExportDescriptor rewrittenDescriptor;
            if(exportDescriptor instanceof FuncExportDescriptor)
                rewrittenDescriptor = new FuncExportDescriptor(functionAddresses[(int) ((FuncExportDescriptor) exportDescriptor).getFuncIdx()]);
            else if(exportDescriptor instanceof TableExportDescriptor)
                rewrittenDescriptor = new TableExportDescriptor(tableAddresses[(int) ((TableExportDescriptor) exportDescriptor).getTableIdx()]);
            else if(exportDescriptor instanceof MemExportDescriptor)
                rewrittenDescriptor = new MemExportDescriptor(memoryAddresses[(int) ((MemExportDescriptor) exportDescriptor).getMemIdx()]);
            else if(exportDescriptor instanceof GlobalExportDescriptor)
                rewrittenDescriptor = new GlobalExportDescriptor(globalAddresses[(int) ((GlobalExportDescriptor) exportDescriptor).getGlobalIdx()]);
            else
                throw new LinkingException();
            exports[i] = new ExportDef(exportDef.getName(), rewrittenDescriptor);
        }
        store.loadModule(getName(), moduleInstance);
        return moduleInstance;
    }

    public void instantiate(final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        virtualMachine.getStack().push(new Activation(ImmutableList.of(), moduleInstance, 0, null));
        instantiateGlobals(virtualMachine, moduleInstance);
        // TODO: Finish this
        virtualMachine.getStack().pop(Activation.class);
    }

    private void instantiateGlobals(final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        for(int i = 0; i < globals.size(); i++) {
            final GlobalDef globalDef = globals.get(i);
            final int globalAddress = moduleInstance.getGlobalAddresses()[i + moduleInstance.getGlobalAddresses().length - globals.size()];
            final GlobalInstance globalInstance = virtualMachine.getStore().getGlobals().get(globalAddress);
            globalInstance.setValue(virtualMachine.evaluate(globalDef.getInit(), 2048, globalDef.getType().getValType().getTypeClass()));
        }
    }

    protected String getName() {
        return "__default__";
    }
}
