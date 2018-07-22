package tv.v1x1.modules.channel.wasm.vm.instructions.control;

import tv.v1x1.modules.channel.wasm.vm.decoder.DecodeException;
import tv.v1x1.modules.channel.wasm.vm.runtime.Instruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.runtime.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.stack.Activation;
import tv.v1x1.modules.channel.wasm.vm.store.FunctionInstance;
import tv.v1x1.modules.channel.wasm.vm.store.TableInstance;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ElemType;
import tv.v1x1.modules.channel.wasm.vm.validation.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.validation.TableType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;
import tv.v1x1.modules.channel.wasm.vm.validation.WebAssemblyValidationStack;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

public class CallIndirectInstruction extends Instruction {
    private I32 typeIdx;

    @Override
    public void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException {
        typeIdx = I32.decodeU(dataInputStream);
        if(dataInputStream.readUnsignedByte() != 0)
            throw new DecodeException();
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        if(context.getTables().size() == 0)
            throw new ValidationException();
        final TableType tableType = context.getTables().get(0);
        if(tableType.getElemType() != ElemType.ANY_FUNC)
            throw new ValidationException();
        if(context.getTypes().size() <= typeIdx.getValU())
            throw new ValidationException();
        final FunctionType functionType = context.getTypes().get((int) typeIdx.getValU());
        stack.popOperand(ValType.I32);
        stack.popOperands(functionType.getParameters().toArray(new ValType[] {}));
        stack.pushOperands(functionType.getReturnTypes().toArray(new ValType[] {}));
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final Activation currentFrame = virtualMachine.getCurrentActivation();
        final int tableAddress = currentFrame.getModule().getTableAddresses()[0];
        final TableInstance tableInstance = virtualMachine.getStore().getTables().get(tableAddress);
        final FunctionType expectedFunctionType = currentFrame.getModule().getTypes()[(int) typeIdx.getValU()];
        final long idx = virtualMachine.getStack().pop(I32.class).getValU();
        if(tableInstance.getElements().size() <= idx)
            throw new TrapException("Invalid table index");
        if(!tableInstance.getElements().get((int) idx).isPresent())
            throw new TrapException("Jump to uninitialized table entry");
        final int functionAddress = tableInstance.getElements().get((int) idx).get();
        final FunctionInstance functionInstance = virtualMachine.getStore().getFunctions().get(functionAddress);
        final FunctionType actualFunctionType = functionInstance.getType();
        if(!Objects.equals(expectedFunctionType, actualFunctionType))
            throw new TrapException("Invalid function type");
        invoke(virtualMachine, functionAddress, nextInstruction);
    }
}
