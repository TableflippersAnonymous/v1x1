package tv.v1x1.modules.channel.wasm.vm;

public class InstructionSequence {
    private final Instruction first;
    private final Instruction last;
    private final int count;

    public InstructionSequence(final Instruction first, final Instruction last, final int count) {
        this.first = first;
        this.last = last;
        this.count = count;
    }

    public Instruction getFirst() {
        return first;
    }

    public Instruction getLast() {
        return last;
    }

    public int getCount() {
        return count;
    }
}
