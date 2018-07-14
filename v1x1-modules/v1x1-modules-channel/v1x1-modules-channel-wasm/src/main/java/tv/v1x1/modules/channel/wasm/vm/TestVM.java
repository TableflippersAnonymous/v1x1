package tv.v1x1.modules.channel.wasm.vm;

import tv.v1x1.modules.channel.wasm.api.V1x1WebAssemblyModuleDef;
import tv.v1x1.modules.channel.wasm.vm.decoder.ModuleDef;

public class TestVM {
    public static void main(final String[] args) {
        try {
            final long start = System.nanoTime();
            final WebAssemblyVirtualMachine virtualMachine = WebAssemblyVirtualMachine.build();
            final ModuleDef v1x1ModuleDef = new V1x1WebAssemblyModuleDef(null);
            final ModuleDef moduleDef = ModuleDef.fromString("main",
                    "AGFzbQEAAAABCQJgAn9/AGAAAAISAQNlbnYKdjF4MV93cml0ZQAAAwMCAQEEBQFwAQEBBQMBAAIG" +
                    "FQN/AUGQiAQLfwBBkIgEC38AQY0ICwcsBARtYWluAAIGbWVtb3J5AgALX19oZWFwX2Jhc2UDAQpf" +
                    "X2RhdGFfZW5kAwIIAQIKDgICAAsJAEGACEEMEAALCxQBAEGACAsNSGVsbG8gV29ybGQKAAA6BG5h" +
                    "bWUBJgMACnYxeDFfd3JpdGUBEV9fd2FzbV9jYWxsX2N0b3JzAgRtYWluAgsDAAIAAAEAAQACAA==");
            virtualMachine.loadModules(v1x1ModuleDef, moduleDef);
            for(int i = 0; i < 10000; i++) {
                virtualMachine.callExport("env", "main", 65536);
            }
            System.out.println("Time: " + (System.nanoTime() - start));
        } catch(final Exception e) {
            e.printStackTrace();
        }
    }
}
