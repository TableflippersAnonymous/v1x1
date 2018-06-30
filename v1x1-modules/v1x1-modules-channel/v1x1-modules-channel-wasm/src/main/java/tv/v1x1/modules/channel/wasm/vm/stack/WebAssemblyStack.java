package tv.v1x1.modules.channel.wasm.vm.stack;

import tv.v1x1.modules.channel.wasm.vm.TrapException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class WebAssemblyStack {
    private static final int MAX_SIZE = 1024;

    private Deque<StackElement> stack = new ArrayDeque<>(MAX_SIZE);

    public void push(final StackElement val) throws TrapException {
        if(stack.size() >= MAX_SIZE)
            throw new TrapException("Call stack exhausted");
        stack.push(val);
    }

    public StackElement pop() throws TrapException {
        try {
            return stack.pop();
        } catch(final NoSuchElementException e) {
            throw new TrapException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends StackElement> T pop(final Class<T> clazz) throws TrapException {
        final StackElement element = pop();
        if(!clazz.isInstance(element))
            throw new TrapException("Invalid value on stack");
        return (T) element;
    }

    @SuppressWarnings("unchecked")
    public <T extends StackElement> T popUntil(final Class<T> clazz) throws TrapException {
        while(stack.size() > 0) {
            final StackElement element = pop();
            if(clazz.isInstance(element))
                return (T) element;
        }
        throw new TrapException("Invalid frame on stack");
    }
}
