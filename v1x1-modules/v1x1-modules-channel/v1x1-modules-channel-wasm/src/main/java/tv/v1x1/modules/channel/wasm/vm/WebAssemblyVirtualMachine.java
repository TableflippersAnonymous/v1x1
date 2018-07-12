package tv.v1x1.modules.channel.wasm.vm;

import tv.v1x1.modules.channel.wasm.vm.stack.Activation;
import tv.v1x1.modules.channel.wasm.vm.stack.WebAssemblyStack;
import tv.v1x1.modules.channel.wasm.vm.store.WebAssemblyStore;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;

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

    public <T extends WebAssemblyType> T evaluate(final Instruction instructions, final int maxInstructions, final Class<T> typeClass) throws TrapException {
        setNextInstruction(instructions);
        execute(maxInstructions);
        return stack.pop(typeClass);
    }

    private void execute(final int maxInstructions) throws TrapException {
        for(int i = 0; i < maxInstructions; i++) {
            if(nextInstruction == null)
                return;
            final Instruction instruction = nextInstruction;
            nextInstruction = instruction.nextInstruction;
            instruction.execute(this);
        }
        throw new TrapException("Max instruction count exceeded");
    }
}
