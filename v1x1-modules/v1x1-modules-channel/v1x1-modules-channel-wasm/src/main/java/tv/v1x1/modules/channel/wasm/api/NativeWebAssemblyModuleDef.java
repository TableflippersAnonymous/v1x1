package tv.v1x1.modules.channel.wasm.api;

import com.google.common.collect.ImmutableList;
import tv.v1x1.modules.channel.wasm.ExecutionEnvironment;
import tv.v1x1.modules.channel.wasm.vm.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.ModuleInstance;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.decoder.ExportDef;
import tv.v1x1.modules.channel.wasm.vm.decoder.FuncExportDescriptor;
import tv.v1x1.modules.channel.wasm.vm.decoder.FunctionDef;
import tv.v1x1.modules.channel.wasm.vm.decoder.ModuleDef;
import tv.v1x1.modules.channel.wasm.vm.store.FunctionInstance;
import tv.v1x1.modules.channel.wasm.vm.store.NativeFunctionInstance;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NativeWebAssemblyModuleDef extends ModuleDef {
    public NativeWebAssemblyModuleDef(final ExecutionEnvironment executionEnvironment, final String name, final NativeFunctionSpec[] functions) {
        super(
                name,
                ImmutableList.copyOf(Arrays.stream(functions)
                        .map(NativeFunctionSpec::getFunctionType)
                        .collect(Collectors.toList())),
                ImmutableList.copyOf(IntStream.range(0, functions.length)
                        .mapToObj(i -> new NativeFunctionDef(i, ImmutableList.of(), executionEnvironment, functions[i].getFunction()))
                        .collect(Collectors.toList())),
                ImmutableList.of(),
                ImmutableList.of(),
                ImmutableList.of(),
                ImmutableList.of(),
                ImmutableList.of(),
                Optional.empty(),
                ImmutableList.of(),
                ImmutableList.copyOf(IntStream.range(0, functions.length)
                        .mapToObj(i -> new ExportDef(functions[i].getName(), new FuncExportDescriptor(i)))
                        .collect(Collectors.toList()))
        );
    }

    protected static class NativeFunctionSpec {
        private final String name;
        private final FunctionType functionType;
        private final NativeFunction function;

        public NativeFunctionSpec(final String name, final FunctionType functionType, final NativeFunction function) {
            this.name = name;
            this.functionType = functionType;
            this.function = function;
        }

        public String getName() {
            return name;
        }

        public FunctionType getFunctionType() {
            return functionType;
        }

        public NativeFunction getFunction() {
            return function;
        }
    }

    @FunctionalInterface
    protected interface NativeFunction {
        void invoke(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance previousModule) throws TrapException;
    }

    private static class NativeFunctionDef extends FunctionDef {
        private final ExecutionEnvironment executionEnvironment;
        private final NativeFunction nativeFunction;

        public NativeFunctionDef(final int typeIdx, final List<ValType> locals, final ExecutionEnvironment executionEnvironment, final NativeFunction nativeFunction) {
            super(typeIdx, locals, null);
            this.executionEnvironment = executionEnvironment;
            this.nativeFunction = nativeFunction;
        }

        @Override
        public FunctionInstance instantiate(final ModuleInstance moduleInstance) {
            return new NativeFunctionInstance(moduleInstance.getTypes()[getTypeIdx()], getLocals(), moduleInstance) {
                @Override
                public void invoke(final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance previousModule) throws TrapException {
                    nativeFunction.invoke(executionEnvironment, virtualMachine, previousModule);
                    Instruction.exitFrame(virtualMachine);
                }
            };
        }
    }
}
