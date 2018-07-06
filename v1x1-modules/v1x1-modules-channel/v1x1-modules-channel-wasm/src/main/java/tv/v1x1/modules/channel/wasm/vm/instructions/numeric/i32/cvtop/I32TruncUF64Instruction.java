package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.cvtop;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.F64CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.F64;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

public class I32TruncUF64Instruction extends F64CvtOpInstruction<I32> {
    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        super.validate(stack, context);
        stack.pushOperand(ValType.I32);
    }

    @Override
    public I32 op(final F64 val) throws TrapException {
        return val.truncUI32();
    }
}
