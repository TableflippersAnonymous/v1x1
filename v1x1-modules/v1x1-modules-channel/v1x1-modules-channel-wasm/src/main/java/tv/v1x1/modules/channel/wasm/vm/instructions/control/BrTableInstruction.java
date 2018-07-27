package tv.v1x1.modules.channel.wasm.vm.instructions.control;

import tv.v1x1.modules.channel.wasm.vm.decoder.DecodeException;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.runtime.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationFrame;
import tv.v1x1.modules.channel.wasm.vm.validation.WebAssemblyValidationStack;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BrTableInstruction extends BrInstruction {
    private final List<I32> labels = new ArrayList<>();
    private I32 defaultLabel;

    @Override
    public void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException {
        final long count = I32.decodeU(dataInputStream).getValU();
        if(count > 256)
            throw new DecodeException();
        for(int i = 0; i < count; i++)
            labels.add(I32.decodeU(dataInputStream));
        defaultLabel = I32.decodeU(dataInputStream);
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        if(stack.controlSize() < defaultLabel.getValU())
            throw new ValidationException();
        final ValidationFrame defaultValidationFrame = stack.getControl((int) defaultLabel.getValU());
        for(final I32 label : labels)
            if(stack.controlSize() < label.getValU() || !stack.getControl((int) label.getValU()).getLabelTypes().equals(defaultValidationFrame.getLabelTypes()))
                throw new ValidationException();
        stack.popOperand(ValType.I32);
        stack.popOperands(defaultValidationFrame.getLabelTypes().toArray(new ValType[] {}));
        stack.setUnreachable();
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final long val = virtualMachine.getStack().pop(I32.class).getValU();
        if(val < labels.size())
            labelIndex = labels.get((int) val);
        else
            labelIndex = defaultLabel;
        super.execute(virtualMachine);
    }
}
