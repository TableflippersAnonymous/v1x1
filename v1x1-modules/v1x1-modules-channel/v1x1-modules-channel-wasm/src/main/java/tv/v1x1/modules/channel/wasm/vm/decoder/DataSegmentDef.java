package tv.v1x1.modules.channel.wasm.vm.decoder;

import com.google.common.collect.ImmutableList;
import tv.v1x1.modules.channel.wasm.vm.runtime.Instruction;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;
import tv.v1x1.modules.channel.wasm.vm.validation.WebAssemblyValidationStack;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataSegmentDef {
    private final long memIdx;
    private final Instruction offset;
    private final byte[] init;

    public DataSegmentDef(final long memIdx, final Instruction offset, final byte[] init) {
        this.memIdx = memIdx;
        this.offset = offset;
        this.init = init;
    }

    public static List<DataSegmentDef> decodeVec(final DataInputStream dataInputStream) throws IOException {
        final List<DataSegmentDef> ret = new ArrayList<>();
        final I32 count = I32.decodeU(dataInputStream);
        for(long i = 0; i < count.getValU(); i++)
            ret.add(decode(dataInputStream));
        return ret;
    }

    public static DataSegmentDef decode(final DataInputStream dataInputStream) throws IOException {
        final long memIdx = I32.decodeU(dataInputStream).getValU();
        final Instruction instruction = Instruction.decodeSequence(dataInputStream, false, false).getFirst();
        final byte[] init = ModuleDef.decodeByteVec(dataInputStream);
        return new DataSegmentDef(memIdx, instruction, init);
    }

    public void validate(final Context context) throws ValidationException {
        if(context.getMemories().size() <= memIdx)
            throw new ValidationException();
        final WebAssemblyValidationStack stack = new WebAssemblyValidationStack();
        stack.pushControl(ImmutableList.of(), ImmutableList.of(ValType.I32));
        Instruction.validateSequence(stack, context, offset);
        if(!Instruction.isConstantSequence(offset))
            throw new ValidationException();
    }

    public long getMemIdx() {
        return memIdx;
    }

    public Instruction getOffset() {
        return offset;
    }

    public byte[] getInit() {
        return init;
    }
}
