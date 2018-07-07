package tv.v1x1.modules.channel.wasm.vm.instructions.control;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.Instruction;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyValidationStack;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class ElseInstruction extends Instruction {
    @Override
    public void decode(final DataInputStream dataInputStream) throws IOException {
        /* No action */
    }

    @Override
    public void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException {
        final List<ValType> results = stack.popControl();
        stack.pushControl(results, results);
    }

    @Override
    public void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        exit(virtualMachine);
    }
}
