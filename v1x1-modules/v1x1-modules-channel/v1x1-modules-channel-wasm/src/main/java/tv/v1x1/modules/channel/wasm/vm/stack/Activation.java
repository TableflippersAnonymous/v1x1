package tv.v1x1.modules.channel.wasm.vm.stack;

import tv.v1x1.modules.channel.wasm.vm.runtime.Instruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.ModuleInstance;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;

import java.util.List;

public class Activation implements StackElement {
    private final List<WebAssemblyType> locals;
    private final ModuleInstance module;
    private final int arity;
    private final Instruction nextInstruction;
    private final int funcIdx;

    public Activation(final List<WebAssemblyType> locals, final ModuleInstance module, final int arity, final Instruction nextInstruction, final int funcIdx) {
        this.locals = locals;
        this.module = module;
        this.arity = arity;
        this.nextInstruction = nextInstruction;
        this.funcIdx = funcIdx;
    }

    @SuppressWarnings("unchecked")
    public <T extends WebAssemblyType> T getLocal(final int idx, final Class<T> clazz) throws TrapException {
        try {
            final WebAssemblyType val = locals.get(idx);
            if(clazz.isInstance(val))
                return (T) val;
            throw new TrapException("Invalid type for local " + idx);
        } catch(final IndexOutOfBoundsException e) {
            throw new TrapException("Invalid local " + idx);
        }
    }

    public void setLocal(final int idx, final WebAssemblyType val) throws TrapException {
        getLocal(idx, val.getClass());
        locals.set(idx, val);
    }

    public ModuleInstance getModule() {
        return module;
    }

    public int getArity() {
        return arity;
    }

    public Instruction getNextInstruction() {
        return nextInstruction;
    }

    @Override
    public String toString() {
        return "Activation{" +
                "locals=" + locals +
                ", arity=" + arity +
                ", nextInstruction=" + nextInstruction +
                ", funcIdx=" + funcIdx +
                '}';
    }
}
