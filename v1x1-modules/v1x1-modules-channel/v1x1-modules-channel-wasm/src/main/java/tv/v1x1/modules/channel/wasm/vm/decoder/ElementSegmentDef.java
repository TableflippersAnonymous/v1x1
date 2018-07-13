package tv.v1x1.modules.channel.wasm.vm.decoder;

import com.google.common.collect.ImmutableList;
import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.ElemType;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TableType;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

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
        final Instruction instruction = Instruction.decodeSequence(dataInputStream, false, false).getFirst();
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

    public void validate(final Context context) throws ValidationException {
        if(context.getTables().size() <= tableIdx)
            throw new ValidationException();
        final TableType tableType = context.getTables().get((int) tableIdx);
        if(tableType.getElemType() != ElemType.ANY_FUNC)
            throw new ValidationException();
        final WebAssemblyValidationStack stack = new WebAssemblyValidationStack();
        stack.pushControl(ImmutableList.of(), ImmutableList.of(ValType.I32));
        Instruction.validateSequence(stack, context, offset);
        if(!Instruction.isConstantSequence(offset))
            throw new ValidationException();
        for(final int funcIdx : init)
            if(context.getFuncs().size() <= funcIdx)
                throw new ValidationException();
    }
}
