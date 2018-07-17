package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.store.MemoryInstance;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.util.Arrays;

public abstract class LoadInstruction extends MemoryInstruction {
    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        super.validate(stack, context);
        stack.pushOperand(getType());
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final int memoryAddress = virtualMachine.getCurrentActivation().getModule().getMemoryAddresses()[0];
        final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(memoryAddress);
        final I32 location = virtualMachine.getStack().pop(I32.class);
        final int effectiveAddress = location.add(offset).getVal();
        if(effectiveAddress + getWidth() / 8 > memoryInstance.getData().length)
            throw new TrapException("Invalid memory access: " + effectiveAddress + "/" + getWidth());
        final byte[] bytes = Arrays.copyOfRange(memoryInstance.getData(), effectiveAddress,
                effectiveAddress + getWidth() / 8);
        virtualMachine.getStack().push(convert(bytes));
    }

    protected abstract WebAssemblyType convert(final byte[] bytes);
}
