package tv.v1x1.modules.channel.wasm.vm.instructions.control;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.stack.Label;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class BrInstruction extends Instruction {
    protected I32 labelIndex;

    @Override
    public void decode(final DataInputStream dataInputStream) throws IOException {
        labelIndex = I32.decodeU(dataInputStream);
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        final long label = labelIndex.getValU();
        if(stack.controlSize() < label)
            throw new ValidationException();
        stack.popOperands(stack.getControl((int) label).getLabelTypes().toArray(new ValType[] {}));
        stack.setUnreachable();
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final Label label = virtualMachine.getStack().getLabel((int) labelIndex.getValU());
        final Deque<WebAssemblyType> deque = new ArrayDeque<>();
        for(int i = 0; i < label.getArity(); i++)
            deque.push(virtualMachine.getStack().pop(WebAssemblyType.class));
        for(int i = 0; i <= labelIndex.getValU(); i++)
            virtualMachine.getStack().popUntil(Label.class);
        while(deque.size() > 0)
            virtualMachine.getStack().push(deque.pop());
        virtualMachine.setNextInstruction(label.getBody());
    }
}
