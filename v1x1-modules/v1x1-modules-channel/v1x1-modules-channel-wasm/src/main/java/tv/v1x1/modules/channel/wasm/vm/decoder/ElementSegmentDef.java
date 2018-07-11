package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElementSegmentDef {
    private final long tableIdx;
    private final Instruction offset;
    private final List<Integer> init;

    public ElementSegmentDef(final long tableIdx, final Instruction offset, final List<Integer> init) {
        this.tableIdx = tableIdx;
        this.offset = offset;
        this.init = init;
    }

    public static List<ElementSegmentDef> decodeVec(final DataInputStream dataInputStream) throws IOException {
        final List<ElementSegmentDef> ret = new ArrayList<>();
        final I32 count = I32.decodeU(dataInputStream);
        for(long i = 0; i < count.getValU(); i++)
            ret.add(decode(dataInputStream));
        return ret;
    }

    public static ElementSegmentDef decode(final DataInputStream dataInputStream) throws IOException {
        final long tableIdx = I32.decodeU(dataInputStream).getValU();
        final Instruction instruction = Instruction.decodeSequence(dataInputStream, false).getFirst();
        final List<Integer> init = ModuleDef.decodeU32Vec(dataInputStream);
        return new ElementSegmentDef(tableIdx, instruction, init);
    }

    public long getTableIdx() {
        return tableIdx;
    }

    public Instruction getOffset() {
        return offset;
    }

    public List<Integer> getInit() {
        return init;
    }
}
