package tv.v1x1.modules.channel.wasm.vm.instructions.control;

import com.google.common.collect.ImmutableList;
import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.InstructionSequence;
import tv.v1x1.modules.channel.wasm.vm.ResultType;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.stack.Label;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class IfInstruction extends Instruction {
    private Optional<ValType> returnType;
    private Instruction ifBody;
    private Instruction elseBody;

    @Override
    public void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException {
        returnType = ValType.decodeOptional(dataInputStream);
        final InstructionSequence sequence = decodeSequence(dataInputStream, true, true);
        ifBody = sequence.getFirst();
        if(sequence.getLast() instanceof ElseInstruction)
            elseBody = decodeSequence(dataInputStream, false, true).getFirst();
        else
            elseBody = new EndInstruction();
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        final List<ValType> list = returnType.map(ImmutableList::of).orElseGet(ImmutableList::of);
        stack.popOperands(ValType.I32);
        final Context newContext = context.addLabel(new ResultType(list));
        stack.pushControl(list, list);
        validateSequence(stack, newContext, ifBody);
        stack.pushControl(list, list);
        validateSequence(stack, newContext, elseBody);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final I32 condition = virtualMachine.getStack().pop(I32.class);
        final Label label = new Label(this, returnType.isPresent() ? 1 : 0, nextInstruction, nextInstruction);
        if(condition.eqz() == I32.ZERO)
            enter(virtualMachine, label, ifBody);
        else
            enter(virtualMachine, label, elseBody);
    }
}
