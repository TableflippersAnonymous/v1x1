package tv.v1x1.modules.channel.wasm.vm.stack;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.modules.channel.wasm.vm.TrapException;

import java.lang.invoke.MethodHandles;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class WebAssemblyStack {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int MAX_SIZE = 1024;

    private Deque<StackElement> stack = new ArrayDeque<>(MAX_SIZE);
    private Deque<Activation> frames = new ArrayDeque<>();

    public void push(final StackElement val) throws TrapException {
        LOG.info("push({})", val);
        if(stack.size() >= MAX_SIZE)
            throw new TrapException("Call stack exhausted");
        stack.push(val);
        if(val instanceof Activation)
            frames.push((Activation) val);
    }

    public StackElement pop() throws TrapException {
        try {
            final StackElement element = stack.pop();
            if(element == getCurrentFrame())
                frames.pop();
            LOG.info("pop({})", element);
            return element;
        } catch(final NoSuchElementException e) {
            throw new TrapException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends StackElement> T pop(final Class<T> clazz) throws TrapException {
        final StackElement element = pop();
        if(!clazz.isInstance(element))
            throw new TrapException("Invalid value on stack: expected=" + clazz.getCanonicalName() + " got=" + element.getClass().getCanonicalName());
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

    public Activation getCurrentFrame() {
        return frames.peek();
    }

    public Label getLabel(final int val) {
        /* Disabling inspection because this is more efficient than what is recommended */
        //noinspection StaticPseudoFunctionalStyleMethod
        return Iterables.get(Iterables.filter(stack, Label.class), val);
    }

    @Override
    public String toString() {
        return "WebAssemblyStack{" +
                "stack=" + stack +
                ", frames=" + frames +
                '}';
    }

    public String dumpString() {
        return "== WebAssemblyStack ==\n" +
                "Activations:\n" +
                "- " + Joiner.on("\n- ").join(frames) + "\n" +
                "\n" +
                "Full Stack:\n" +
                "- " + Joiner.on("\n- ").join(stack);
    }
}
