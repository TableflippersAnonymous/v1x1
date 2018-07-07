package tv.v1x1.modules.channel.wasm.vm;

public class InstructionSequence {
    private final Instruction first;
    private final Instruction last;

    public InstructionSequence(final Instruction first, final Instruction last) {
        this.first = first;
        this.last = last;
    }

    public Instruction getFirst() {
        return first;
    }

    public Instruction getLast() {
        return last;
    }
}
