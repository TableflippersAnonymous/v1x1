package tv.v1x1.modules.channel.wasm.vm;

import tv.v1x1.modules.channel.wasm.vm.decoder.ModuleDef;
import tv.v1x1.modules.channel.wasm.vm.stack.Activation;
import tv.v1x1.modules.channel.wasm.vm.stack.WebAssemblyStack;
import tv.v1x1.modules.channel.wasm.vm.store.LinkingException;
import tv.v1x1.modules.channel.wasm.vm.store.WebAssemblyStore;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

public class WebAssemblyVirtualMachine {
    private final WebAssemblyStack stack;
    private final WebAssemblyStore store;
    private Instruction nextInstruction;

    public WebAssemblyVirtualMachine(final WebAssemblyStack stack, final WebAssemblyStore store) {
        this.stack = stack;
        this.store = store;
    }

    public static WebAssemblyVirtualMachine build() {
        return new WebAssemblyVirtualMachine(new WebAssemblyStack(), new WebAssemblyStore());
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
        execute(maxInstructions, false);
        return stack.pop(typeClass);
    }

    public void execute(final int maxInstructions) throws TrapException {
        execute(maxInstructions, true);
    }

    public void execute(final int maxInstructions, final boolean exitFrames) throws TrapException {
        for(int i = 0; i < maxInstructions; i++) {
            //noinspection StatementWithEmptyBody
            while(exitFrames && nextInstruction == null && Instruction.exitFrame(this))
                /* Loop */;
            if(nextInstruction == null)
                return;
            final Instruction instruction = nextInstruction;
            nextInstruction = instruction.nextInstruction;
            instruction.execute(this);
        }
        throw new TrapException("Max instruction count exceeded");
    }

    public void loadModules(final ModuleDef... moduleDefs) throws ValidationException, LinkingException, TrapException {
        for(final ModuleDef moduleDef : moduleDefs) {
            moduleDef.validate();
            final ModuleInstance moduleInstance = moduleDef.allocate(getStore());
            moduleDef.instantiate(this, moduleInstance);
        }
    }

    public void callExport(final String module, final String name, final int maxInstructions) throws TrapException {
        final int functionAddress = store.getExportFunction(module, name);
        Instruction.invoke(this, functionAddress, null);
        execute(maxInstructions);
    }
}
