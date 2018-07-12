package tv.v1x1.modules.channel.wasm.vm.decoder;

import com.google.common.collect.ImmutableList;
import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.DecodeException;
import tv.v1x1.modules.channel.wasm.vm.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.ResultType;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FunctionDef {
    private static final int MAX_LOCALS = 2048;

    private final int typeIdx;
    private final List<ValType> locals;
    private final Instruction body;

    public FunctionDef(final int typeIdx, final List<ValType> locals, final Instruction body) {
        this.typeIdx = typeIdx;
        this.locals = locals;
        this.body = body;
    }

    public static List<FunctionDef> decodeVec(final DataInputStream dataInputStream, final List<Integer> types) throws IOException {
        final List<FunctionDef> ret = new ArrayList<>();
        final I32 count = I32.decodeU(dataInputStream);
        if(count.getValU() != types.size())
            throw new DecodeException("Invalid codesec size");
        for(long i = 0; i < count.getValU(); i++)
            ret.add(decode(dataInputStream, types.get((int) i)));
        return ImmutableList.copyOf(ret);
    }

    public static FunctionDef decode(final DataInputStream dataInputStream, final int typeIdx) throws IOException {
        I32.decodeU(dataInputStream); // Size
        final List<ValType> locals = new ArrayList<>();
        final long groupCount = I32.decodeU(dataInputStream).getValU();
        for(long groupIter = 0; groupIter < groupCount; groupIter++) {
            final long count = I32.decodeU(dataInputStream).getValU();
            final ValType valType = ValType.decode(dataInputStream);
            for(long iter = 0; iter < count; iter++) {
                locals.add(valType);
                if(locals.size() > MAX_LOCALS)
                    throw new DecodeException("Too many locals");
            }
        }
        final Instruction instruction = Instruction.decodeSequence(dataInputStream, false).getFirst();
        return new FunctionDef(typeIdx, locals, instruction);
    }

    public int getTypeIdx() {
        return typeIdx;
    }

    public List<ValType> getLocals() {
        return locals;
    }

    public Instruction getBody() {
        return body;
    }

    public void validate(final Context context) throws ValidationException {
        if(context.getTypes().size() <= typeIdx)
            throw new ValidationException();
        final FunctionType type = context.getTypes().get(typeIdx);
        final List<ValType> locals = new ArrayList<>(type.getParameters());
        locals.addAll(getLocals());
        final Context newContext = new Context(context.getTypes(), context.getFuncs(), context.getTables(),
                context.getMemories(), context.getGlobals(), locals, ImmutableList.of(new ResultType(type.getReturnTypes())),
                Optional.of(new ResultType(type.getReturnTypes())));
        final WebAssemblyValidationStack stack = new WebAssemblyValidationStack();
        stack.pushControl(type.getReturnTypes(), type.getReturnTypes());
        Instruction.validateSequence(stack, newContext, body);
    }
}
