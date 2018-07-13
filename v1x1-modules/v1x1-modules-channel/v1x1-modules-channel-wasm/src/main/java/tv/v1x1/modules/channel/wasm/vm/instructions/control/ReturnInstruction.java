package tv.v1x1.modules.channel.wasm.vm.instructions.control;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.stack.Activation;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class ReturnInstruction extends Instruction {
    @Override
    public void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException {
        /* No action */
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        if(!context.getRetType().isPresent())
            throw new ValidationException();
        stack.popOperands(context.getRetType().get().toArray(new ValType[] {}));
        stack.setUnreachable();
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final Deque<WebAssemblyType> retValues = new ArrayDeque<>();
        final Activation currentFrame = virtualMachine.getCurrentActivation();
        for(int i = 0; i < currentFrame.getArity(); i++)
            retValues.push(virtualMachine.getStack().pop(WebAssemblyType.class));
        virtualMachine.getStack().popUntil(Activation.class);
        while(!retValues.isEmpty())
            virtualMachine.getStack().push(retValues.pop());
        virtualMachine.setNextInstruction(currentFrame.getNextInstruction());
    }
}
