package tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.cvtop;

import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.F64CvtOpInstruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.types.F64;
import tv.v1x1.modules.channel.wasm.vm.types.I64;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;
import tv.v1x1.modules.channel.wasm.vm.validation.WebAssemblyValidationStack;

public class I64TruncUF64Instruction extends F64CvtOpInstruction<I64> {
    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        super.validate(stack, context);
        stack.pushOperand(ValType.I64);
    }

    @Override
    public I64 op(final F64 val) throws TrapException {
        return val.truncUI64();
    }
}
