package tv.v1x1.modules.channel.wasm.vm.instructions.store.memory;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.store.MemoryInstance;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

public abstract class StoreInstruction<T extends WebAssemblyType> extends MemoryInstruction {
    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        stack.popOperand(getType());
        super.validate(stack, context);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final int memoryAddress = virtualMachine.getCurrentActivation().getModule().getMemoryAddresses()[0];
        final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(memoryAddress);
        final T val = virtualMachine.getStack().pop(getTypeClass());
        final I32 location = virtualMachine.getStack().pop(I32.class);
        final int effectiveAddress = location.add(offset).getVal();
        if(effectiveAddress + getWidth() / 8 > memoryInstance.getData().length)
            throw new TrapException("Invalid memory access");
        final byte[] bytes = getBytes(val);
        for(int i = 0; i < bytes.length; i++)
            memoryInstance.getData()[effectiveAddress + i] = bytes[i];
    }

    protected abstract Class<T> getTypeClass();

    protected abstract byte[] getBytes(final T val);
}
