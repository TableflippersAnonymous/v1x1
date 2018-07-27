package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.cvtop;

import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.I64CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.I64;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;
import tv.v1x1.modules.channel.wasm.vm.validation.WebAssemblyValidationStack;

public class I32WrapI64Instruction extends I64CvtOpInstruction<I32> {
    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        super.validate(stack, context);
        stack.pushOperand(ValType.I32);
    }

    @Override
    public I32 op(final I64 val) {
        return val.wrap();
    }
}
