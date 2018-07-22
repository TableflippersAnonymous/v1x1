package tv.v1x1.modules.channel.wasm.vm.instructions.control;

import com.google.common.collect.ImmutableList;
import tv.v1x1.modules.channel.wasm.vm.runtime.Instruction;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.runtime.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.stack.Label;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.ResultType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;
import tv.v1x1.modules.channel.wasm.vm.validation.WebAssemblyValidationStack;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BlockInstruction extends Instruction {
    private Optional<ValType> returnType;
    private Instruction innerInstructions;

    @Override
    public void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException {
        returnType = ValType.decodeOptional(dataInputStream);
        innerInstructions = decodeSequence(dataInputStream, false, true).getFirst();
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        final List<ValType> list = returnType.map(ImmutableList::of).orElseGet(ImmutableList::of);
        stack.pushControl(list, list);
        final Context newContext = context.addLabel(new ResultType(list));
        validateSequence(stack, newContext, innerInstructions);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        enter(virtualMachine, new Label(this, returnType.isPresent() ? 1 : 0, nextInstruction, nextInstruction), innerInstructions);
    }
}
