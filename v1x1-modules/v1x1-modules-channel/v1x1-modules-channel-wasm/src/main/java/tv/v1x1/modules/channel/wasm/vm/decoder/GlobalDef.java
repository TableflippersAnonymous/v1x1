package tv.v1x1.modules.channel.wasm.vm.decoder;

import com.google.common.collect.ImmutableList;
import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.GlobalType;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GlobalDef {
    private final GlobalType type;
    private final Instruction init;

    public GlobalDef(final GlobalType type, final Instruction init) {
        this.type = type;
        this.init = init;
    }

    public static List<GlobalDef> decodeVec(final DataInputStream dataInputStream) throws IOException {
        final List<GlobalDef> ret = new ArrayList<>();
        final I32 count = I32.decodeU(dataInputStream);
        for(long i = 0; i < count.getValU(); i++)
            ret.add(decode(dataInputStream));
        return ret;
    }

    private static GlobalDef decode(final DataInputStream dataInputStream) throws IOException {
        final GlobalType globalType = GlobalType.decode(dataInputStream);
        final Instruction instruction = Instruction.decodeSequence(dataInputStream, false, false).getFirst();
        return new GlobalDef(globalType, instruction);
    }

    public GlobalType getType() {
        return type;
    }

    public Instruction getInit() {
        return init;
    }

    public void validate(final Context context) throws ValidationException {
        type.validate();
        final WebAssemblyValidationStack stack = new WebAssemblyValidationStack();
        stack.pushControl(ImmutableList.of(), ImmutableList.of(type.getValType()));
        Instruction.validateSequence(stack, context, init);
        if(!Instruction.isConstantSequence(init))
            throw new ValidationException();
    }
}
