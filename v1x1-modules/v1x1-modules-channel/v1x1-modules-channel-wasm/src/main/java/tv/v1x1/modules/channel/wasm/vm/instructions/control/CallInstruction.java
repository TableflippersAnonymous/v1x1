package tv.v1x1.modules.channel.wasm.vm.instructions.control;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.stack.Activation;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;

public class CallInstruction extends Instruction {
    private I32 functionIdx;

    @Override
    public void decode(final DataInputStream dataInputStream) throws IOException {
        functionIdx = I32.decodeU(dataInputStream);
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        if(context.getFuncs().size() <= functionIdx.getValU())
            throw new ValidationException();
        final FunctionType functionType = context.getFuncs().get((int) functionIdx.getValU());
        stack.popOperands(functionType.getParameters().toArray(new ValType[] {}));
        stack.pushOperands(functionType.getReturnTypes().toArray(new ValType[] {}));
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final Activation currentFrame = virtualMachine.getCurrentActivation();
        final int functionAddress = currentFrame.getModule().getFunctionAddresses()[(int) functionIdx.getValU()];
        invoke(virtualMachine, functionAddress, nextInstruction);
    }
}
