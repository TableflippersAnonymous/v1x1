package tv.v1x1.modules.channel.wasm.vm.store;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import tv.v1x1.modules.channel.wasm.vm.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.ModuleInstance;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.decoder.ExportDef;
import tv.v1x1.modules.channel.wasm.vm.decoder.FuncExportDescriptor;
import tv.v1x1.modules.channel.wasm.vm.decoder.FuncImportDescriptor;
import tv.v1x1.modules.channel.wasm.vm.decoder.GlobalExportDescriptor;
import tv.v1x1.modules.channel.wasm.vm.decoder.GlobalImportDescriptor;
import tv.v1x1.modules.channel.wasm.vm.decoder.ImportDef;
import tv.v1x1.modules.channel.wasm.vm.decoder.ImportDescriptor;
import tv.v1x1.modules.channel.wasm.vm.decoder.MemExportDescriptor;
import tv.v1x1.modules.channel.wasm.vm.decoder.MemImportDescriptor;
import tv.v1x1.modules.channel.wasm.vm.decoder.ResolvedImports;
import tv.v1x1.modules.channel.wasm.vm.decoder.TableExportDescriptor;
import tv.v1x1.modules.channel.wasm.vm.decoder.TableImportDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebAssemblyStore {
    private final List<FunctionInstance> functions = new ArrayList<>();
    private final List<TableInstance> tables = new ArrayList<>();
    private final List<MemoryInstance> memories = new ArrayList<>();
    private final List<GlobalInstance> globals = new ArrayList<>();
    private final Multimap<String, ModuleInstance> modules = HashMultimap.create();

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

    public ResolvedImports resolveImports(final FunctionType[] types, final List<ImportDef> imports) throws LinkingException {
        final List<Integer> functionAddresses = new ArrayList<>();
        final List<Integer> tableAddresses = new ArrayList<>();
        final List<Integer> memoryAddresses = new ArrayList<>();
        final List<Integer> globalAddresses = new ArrayList<>();

        for(final ImportDef importDef : imports) {
            if(!modules.containsKey(importDef.getModule()))
                throw new LinkingException();
            final ImportDescriptor importDescriptor = importDef.getDescriptor();
            for(final ModuleInstance moduleInstance : modules.get(importDef.getModule()))
                if(importDescriptor instanceof FuncImportDescriptor)
                    functionAddresses.add(resolveImport(types, moduleInstance, importDef.getName(), (FuncImportDescriptor) importDescriptor));
                else if(importDescriptor instanceof TableImportDescriptor)
                    tableAddresses.add(resolveImport(moduleInstance, importDef.getName(), (TableImportDescriptor) importDescriptor));
                else if(importDescriptor instanceof MemImportDescriptor)
                    memoryAddresses.add(resolveImport(moduleInstance, importDef.getName(), (MemImportDescriptor) importDescriptor));
                else if(importDescriptor instanceof GlobalImportDescriptor)
                    globalAddresses.add(resolveImport(moduleInstance, importDef.getName(), (GlobalImportDescriptor) importDescriptor));
                else
                    throw new LinkingException();
        }

        return new ResolvedImports(
                functionAddresses.stream().mapToInt(i -> i).toArray(),
                tableAddresses.stream().mapToInt(i -> i).toArray(),
                memoryAddresses.stream().mapToInt(i -> i).toArray(),
                globalAddresses.stream().mapToInt(i -> i).toArray()
        );
    }

    private int resolveImport(final FunctionType[] types, final ModuleInstance moduleInstance, final String name,
                              final FuncImportDescriptor descriptor) throws LinkingException {
        for(final ExportDef exportDef : moduleInstance.getExports()) {
            if(!exportDef.getName().equals(name))
                continue;
            if(!(exportDef.getDescriptor() instanceof FuncExportDescriptor))
                continue;
            final FuncExportDescriptor funcExportDescriptor = (FuncExportDescriptor) exportDef.getDescriptor();
            if(!functions.get((int) funcExportDescriptor.getFuncIdx()).getType().equals(types[(int) descriptor.getTypeIdx()]))
                continue;
            return (int) funcExportDescriptor.getFuncIdx();
        }
        throw new LinkingException();
    }

    private int resolveImport(final ModuleInstance moduleInstance, final String name,
                              final TableImportDescriptor descriptor) throws LinkingException {
        for(final ExportDef exportDef : moduleInstance.getExports()) {
            if(!exportDef.getName().equals(name))
                continue;
            if(!(exportDef.getDescriptor() instanceof TableExportDescriptor))
                continue;
            final TableExportDescriptor tableExportDescriptor = (TableExportDescriptor) exportDef.getDescriptor();
            if(!tables.get((int) tableExportDescriptor.getTableIdx()).matches(descriptor.getTableType()))
                continue;
            return (int) tableExportDescriptor.getTableIdx();
        }
        throw new LinkingException();
    }

    private int resolveImport(final ModuleInstance moduleInstance, final String name,
                              final MemImportDescriptor descriptor) throws LinkingException {
        for(final ExportDef exportDef : moduleInstance.getExports()) {
            if(!exportDef.getName().equals(name))
                continue;
            if(!(exportDef.getDescriptor() instanceof MemExportDescriptor))
                continue;
            final MemExportDescriptor memExportDescriptor = (MemExportDescriptor) exportDef.getDescriptor();
            if(!memories.get((int) memExportDescriptor.getMemIdx()).matches(descriptor.getMemoryType()))
                continue;
            return (int) memExportDescriptor.getMemIdx();
        }
        throw new LinkingException();
    }

    private int resolveImport(final ModuleInstance moduleInstance, final String name,
                              final GlobalImportDescriptor descriptor) throws LinkingException {
        for(final ExportDef exportDef : moduleInstance.getExports()) {
            if(!exportDef.getName().equals(name))
                continue;
            if(!(exportDef.getDescriptor() instanceof GlobalExportDescriptor))
                continue;
            final GlobalExportDescriptor globalExportDescriptor = (GlobalExportDescriptor) exportDef.getDescriptor();
            if(!globals.get((int) globalExportDescriptor.getGlobalIdx()).matches(descriptor.getGlobalType()))
                continue;
            return (int) globalExportDescriptor.getGlobalIdx();
        }
        throw new LinkingException();
    }

    public int allocateFunction(final FunctionInstance functionInstance) {
        final int ret = functions.size();
        functions.add(functionInstance);
        return ret;
    }

    public int allocateTable(final TableInstance tableInstance) {
        final int ret = tables.size();
        tables.add(tableInstance);
        return ret;
    }

    public int allocateMemory(final MemoryInstance memoryInstance) {
        final int ret = memories.size();
        memories.add(memoryInstance);
        return ret;
    }

    public int allocateGlobal(final GlobalInstance globalInstance) {
        final int ret = globals.size();
        globals.add(globalInstance);
        return ret;
    }

    public void loadModule(final String name, final ModuleInstance moduleInstance) {
        modules.put(name, moduleInstance);
        modules.put("env", moduleInstance);
    }

    public int getExportFunction(final String module, final String name) throws TrapException {
        final List<Integer> list = getAllExportFunction(module, name);
        if(list.size() == 0)
            throw new TrapException("No such method");
        return list.get(0);
    }

    public List<Integer> getAllExportFunction(final String module, final String name) {
        final List<Integer> functions = new ArrayList<>();
        for(final ModuleInstance moduleInstance : modules.get(module))
            for(final ExportDef exportDef : moduleInstance.getExports())
                if(exportDef.getName().equals(name) && exportDef.getDescriptor() instanceof FuncExportDescriptor)
                    functions.add((int) ((FuncExportDescriptor) exportDef.getDescriptor()).getFuncIdx());
        return functions;
    }

    @Override
    public String toString() {
        return "WebAssemblyStore{" +
                "functions=" + functions +
                ", tables=" + tables +
                ", memories=" + memories +
                ", globals=" + globals +
                ", modules=" + modules +
                '}';
    }

    public String dumpString() {
        return "== WebAssemblyStore ==\n" +
                "functions:\n" +
                "- " + Joiner.on("\n - ").join(functions) + "\n" +
                "\n" +
                "tables:\n" +
                "- " + Joiner.on("\n - ").join(tables) + "\n" +
                "\n" +
                "memories:\n" +
                "- " + Joiner.on("\n - ").join(memories) + "\n" +
                "\n" +
                "globals:\n" +
                "- " + Joiner.on("\n - ").join(globals) + "\n" +
                "\n" +
                "modules:\n" +
                Joiner.on("\n").join(modules.entries().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.toList()));
    }
}
