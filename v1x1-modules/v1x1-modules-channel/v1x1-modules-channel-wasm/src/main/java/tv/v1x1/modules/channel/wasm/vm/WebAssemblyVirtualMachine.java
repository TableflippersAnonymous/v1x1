package tv.v1x1.modules.channel.wasm.vm;

import tv.v1x1.modules.channel.wasm.vm.stack.Activation;
import tv.v1x1.modules.channel.wasm.vm.stack.WebAssemblyStack;
import tv.v1x1.modules.channel.wasm.vm.store.WebAssemblyStore;

public class WebAssemblyVirtualMachine {
    private final WebAssemblyStack stack;
    private final WebAssemblyStore store;
    private Instruction nextInstruction;

    public WebAssemblyVirtualMachine(final WebAssemblyStack stack, final WebAssemblyStore store) {
        this.stack = stack;
        this.store = store;
    }

    public WebAssemblyStack getStack() {
        return stack;
    }

    public WebAssemblyStore getStore() {
        return store;
    }

    public Activation getCurrentActivation() {
        return getStack().getCurrentFrame();
    }

    public void setNextInstruction(final Instruction instruction) {
        this.nextInstruction = instruction;
    }
}
