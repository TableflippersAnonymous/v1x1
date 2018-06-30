package tv.v1x1.modules.channel.wasm.vm;

import tv.v1x1.modules.channel.wasm.vm.stack.WebAssemblyStack;

public class WebAssemblyVirtualMachine {
    private final WebAssemblyStack stack;

    public WebAssemblyVirtualMachine(final WebAssemblyStack stack) {
        this.stack = stack;
    }

    public WebAssemblyStack getStack() {
        return stack;
    }
}
