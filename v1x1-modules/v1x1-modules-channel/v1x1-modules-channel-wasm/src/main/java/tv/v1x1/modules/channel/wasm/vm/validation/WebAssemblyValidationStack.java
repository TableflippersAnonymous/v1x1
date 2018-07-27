package tv.v1x1.modules.channel.wasm.vm.validation;

import java.util.*;

public class WebAssemblyValidationStack {
    private final Deque<ValType> operandStack = new ArrayDeque<>();
    private final Deque<ValidationFrame> controlStack = new ArrayDeque<>();

    public void pushOperand(final ValType valType) {
        operandStack.push(valType);
    }

    public ValType popOperand() throws ValidationException {
        if(operandStack.size() == controlStack.peek().getHeight())
            if(controlStack.peek().isUnreachable())
                return ValType.UNKNOWN;
            else
                throw new ValidationException();
        return operandStack.pop();
    }

    public ValType popOperand(final ValType expected) throws ValidationException {
        final ValType actual = popOperand();
        if(actual == ValType.UNKNOWN)
            return expected;
        if(expected == ValType.UNKNOWN)
            return actual;
        if(!actual.equals(expected))
            throw new ValidationException();
        return actual;
    }

    public void pushOperands(final ValType... valTypes) {
        Arrays.asList(valTypes).forEach(this::pushOperand);
    }

    public void popOperands(final ValType... valTypes) throws ValidationException {
        Collections.reverse(Arrays.asList(valTypes));
        for(ValType valType : valTypes) {
            popOperand(valType);
        }
    }

    public void pushControl(final List<ValType> label, final List<ValType> output) {
        controlStack.push(new ValidationFrame(label, output, operandStack.size(), false));
    }

    public List<ValType> popControl() throws ValidationException {
        if(controlStack.isEmpty())
            throw new ValidationException();
        final ValidationFrame frame = controlStack.peek();
        popOperands(frame.getEndTypes().toArray(new ValType[] {}));
        if(operandStack.size() != frame.getHeight())
            throw new ValidationException();
        controlStack.pop();
        return frame.getEndTypes();
    }

    public void setUnreachable() throws ValidationException {
        while(operandStack.size() > controlStack.peek().getHeight())
            popOperand();
        controlStack.peek().setUnreachable();
    }

    public int controlSize() {
        return controlStack.size();
    }

    public ValidationFrame getControl(final int label) {
        return controlStack.toArray(new ValidationFrame[] {})[label];
    }
}
