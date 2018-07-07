package tv.v1x1.modules.channel.wasm.vm.stack;

import tv.v1x1.modules.channel.wasm.vm.Instruction;

public class Label implements StackElement {
    private final int arity;
    private final Instruction body;
    private final Instruction end;

    public Label(final int arity, final Instruction body, final Instruction end) {
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
}
