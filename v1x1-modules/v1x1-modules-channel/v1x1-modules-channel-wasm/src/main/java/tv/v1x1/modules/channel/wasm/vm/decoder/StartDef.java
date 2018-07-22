package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;

public class StartDef {
    private final long funcIdx;

    public StartDef(final long funcIdx) {
        this.funcIdx = funcIdx;
    }

    public static StartDef decode(final DataInputStream dataInputStream) throws IOException {
        return new StartDef(I32.decodeU(dataInputStream).getValU());
    }

    public long getFuncIdx() {
        return funcIdx;
    }

    public void validate(final Context context) throws ValidationException {
        if(context.getFuncs().size() <= funcIdx)
            throw new ValidationException();
        final FunctionType functionType = context.getFuncs().get((int) funcIdx);
        if(functionType.getParameters().size() > 0 || functionType.getReturnTypes().size() > 0)
            throw new ValidationException();
    }
}
