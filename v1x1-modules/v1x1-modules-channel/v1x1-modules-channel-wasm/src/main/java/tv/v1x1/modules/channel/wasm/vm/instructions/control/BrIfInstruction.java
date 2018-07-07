package tv.v1x1.modules.channel.wasm.vm.instructions.control;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

public class BrIfInstruction extends BrInstruction {
    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        final int label = labelIndex.getVal();
        if(label < 0)
            throw new ValidationException();
        if(stack.controlSize() < label)
            throw new ValidationException();
        stack.popOperand(ValType.I32);
        final ValType[] labelTypes = stack.getControl(label).getLabelTypes().toArray(new ValType[] {});
        stack.popOperands(labelTypes);
        stack.pushOperands(labelTypes);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        if(virtualMachine.getStack().pop(I32.class).eqz() == I32.ZERO)
            super.execute(virtualMachine);
    }
}
