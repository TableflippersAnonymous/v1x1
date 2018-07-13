package tv.v1x1.modules.channel.wasm.vm;

import com.google.common.collect.ImmutableList;
import com.google.common.io.BaseEncoding;
import tv.v1x1.modules.channel.wasm.vm.decoder.ExportDef;
import tv.v1x1.modules.channel.wasm.vm.decoder.FuncExportDescriptor;
import tv.v1x1.modules.channel.wasm.vm.decoder.ModuleDef;
import tv.v1x1.modules.channel.wasm.vm.store.MemoryInstance;
import tv.v1x1.modules.channel.wasm.vm.store.NativeFunctionInstance;
import tv.v1x1.modules.channel.wasm.vm.store.WebAssemblyStore;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class TestVM {
    public static class V1x1WriteFunction extends NativeFunctionInstance {
        public V1x1WriteFunction(final ModuleInstance module) {
            super(new FunctionType(ImmutableList.of(ValType.I32, ValType.I32), ImmutableList.of()), ImmutableList.of(), module);
        }

        @Override
        public void invoke(final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance previousModule) throws TrapException {
            final int address = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
            final int length = virtualMachine.getCurrentActivation().getLocal(1, I32.class).getVal();
            final byte[] data = new byte[length];
            final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(previousModule.getMemoryAddresses()[0]);
            System.arraycopy(memoryInstance.getData(), address, data, 0, length);
            try {
                System.out.write(data);
            } catch(IOException e) {
                throw new TrapException(e);
            }
        }
    }

    public static class V1x1Module extends ModuleInstance {
        public V1x1Module(final WebAssemblyStore store) {
            super(
                    new FunctionType[] { null },
                    new int[] { 0 },
                    new int[] {},
                    new int[] {},
                    new int[] {},
                    new ExportDef[] { null }
            );
            final NativeFunctionInstance functionInstance = new V1x1WriteFunction(this);
            getTypes()[0] = functionInstance.getType();
            getFunctionAddresses()[0] = store.allocateFunction(functionInstance);
            getExports()[0] = new ExportDef("v1x1_write", new FuncExportDescriptor(getFunctionAddresses()[0]));
        }
    }

    public static void main(final String[] args) {
        try {
            final long start = System.nanoTime();
            for(int i = 0; i < 1; i++) {
                final WebAssemblyVirtualMachine virtualMachine = WebAssemblyVirtualMachine.build();
                virtualMachine.getStore().loadModule("v1x1", new V1x1Module(virtualMachine.getStore()));
                final byte[] moduleBytes = BaseEncoding.base64().decode(
                        "AGFzbQEAAAABCQJgAn9/AGAAAAISAQNlbnYKdjF4MV93cml0ZQAAAwMCAQEEBQFwAQEBBQMBAAIG" +
                                "FQN/AUGQiAQLfwBBkIgEC38AQY0ICwcsBARtYWluAAIGbWVtb3J5AgALX19oZWFwX2Jhc2UDAQpf" +
                                "X2RhdGFfZW5kAwIIAQIKDgICAAsJAEGACEEMEAALCxQBAEGACAsNSGVsbG8gV29ybGQKAAA6BG5h" +
                                "bWUBJgMACnYxeDFfd3JpdGUBEV9fd2FzbV9jYWxsX2N0b3JzAgRtYWluAgsDAAIAAAEAAQACAA==");
                final ModuleDef moduleDef = ModuleDef.decode(new DataInputStream(new ByteArrayInputStream(moduleBytes)));
                final ModuleInstance moduleInstance = moduleDef.allocate(virtualMachine.getStore());
                moduleDef.instantiate(virtualMachine, moduleInstance);
            }
            System.out.println("Time: " + (System.nanoTime() - start));
        } catch(final Exception e) {
            e.printStackTrace();
        }
    }
}
