package tv.v1x1.modules.channel.wasm.vm.stack;

import tv.v1x1.modules.channel.wasm.vm.Instruction;

public class Label implements StackElement {
    private final int arity;
    private final Instruction body;
    private final Instruction end;
    private final Instruction cause;

    public Label(final Instruction cause, final int arity, final Instruction body, final Instruction end) {
        this.cause = cause;
        this.arity = arity;
        this.body = body;
        this.end = end;
    }

    public int getArity() {
        return arity;
    }

    public Instruction getBody() {
        return body;
    }

    public Instruction getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Label{" +
                "cause=" + cause +
                ", arity=" + arity +
                ", body=" + body +
                ", end=" + end +
                '}';
    }
}
