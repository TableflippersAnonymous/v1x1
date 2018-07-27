package tv.v1x1.modules.channel.wasm.vm.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.modules.channel.wasm.vm.decoder.DecodeException;
import tv.v1x1.modules.channel.wasm.vm.instructions.control.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.F32ConstInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.binop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.cvtop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.relop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f32.unop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.F64ConstInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.binop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.cvtop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.relop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.f64.unop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.I32ConstInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.binop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.cvtop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.relop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.unop.I32ClzInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.unop.I32CtzInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.unop.I32EqzInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i32.unop.I32PopcntInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.I64ConstInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.binop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.cvtop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.relop.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.unop.I64ClzInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.unop.I64CtzInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.unop.I64EqzInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.numeric.i64.unop.I64PopcntInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.stack.locals.GetLocalInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.stack.locals.SetLocalInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.stack.locals.TeeLocalInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.stack.util.DropInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.stack.util.SelectInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.globals.GetGlobalInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.globals.SetGlobalInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.load.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.store.*;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.util.MemoryGrowInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.store.memory.util.MemorySizeInstruction;
import tv.v1x1.modules.channel.wasm.vm.stack.Activation;
import tv.v1x1.modules.channel.wasm.vm.stack.Label;
import tv.v1x1.modules.channel.wasm.vm.stack.StackElement;
import tv.v1x1.modules.channel.wasm.vm.store.FunctionInstance;
import tv.v1x1.modules.channel.wasm.vm.types.WebAssemblyType;
import tv.v1x1.modules.channel.wasm.vm.validation.Context;
import tv.v1x1.modules.channel.wasm.vm.validation.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;
import tv.v1x1.modules.channel.wasm.vm.validation.WebAssemblyValidationStack;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public abstract class Instruction {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /* See https://webassembly.github.io/spec/core/appendix/index-instructions.html */
    private final static Class[] decodeTable = {
            /* 0x00 */ UnreachableInstruction.class, NopInstruction.class, BlockInstruction.class, LoopInstruction.class,
            /* 0x04 */ IfInstruction.class, ElseInstruction.class, null, null,
            /* 0x08 */ null, null, null, EndInstruction.class,
            /* 0x0C */ BrInstruction.class, BrIfInstruction.class, BrTableInstruction.class, ReturnInstruction.class,

            /* 0x10 */ CallInstruction.class, CallIndirectInstruction.class, null, null,
            /* 0x14 */ null, null, null, null,
            /* 0x18 */ null, null, DropInstruction.class, SelectInstruction.class,
            /* 0x1C */ null, null, null, null,

            /* 0x20 */ GetLocalInstruction.class, SetLocalInstruction.class, TeeLocalInstruction.class, GetGlobalInstruction.class,
            /* 0x24 */ SetGlobalInstruction.class, null, null, null,
            /* 0x28 */ I32LoadInstruction.class, I64LoadInstruction.class, F32LoadInstruction.class, F64LoadInstruction.class,
            /* 0x2C */ I32Load8SInstruction.class, I32Load8UInstruction.class, I32Load16SInstruction.class, I32Load16UInstruction.class,

            /* 0x30 */ I64Load8SInstruction.class, I64Load8UInstruction.class, I64Load16SInstruction.class, I64Load16UInstruction.class,
            /* 0x34 */ I64Load32SInstruction.class, I64Load32UInstruction.class, I32StoreInstruction.class, I64StoreInstruction.class,
            /* 0x38 */ F32StoreInstruction.class, F64StoreInstruction.class, I32Store8Instruction.class, I32Store16Instruction.class,
            /* 0x3C */ I64Store8Instruction.class, I64Store16Instruction.class, I64Store32Instruction.class, MemorySizeInstruction.class,

            /* 0x40 */ MemoryGrowInstruction.class, I32ConstInstruction.class, I64ConstInstruction.class, F32ConstInstruction.class,
            /* 0x44 */ F64ConstInstruction.class, I32EqzInstruction.class, I32EqInstruction.class, I32NeInstruction.class,
            /* 0x48 */ I32LtSInstruction.class, I32LtUInstruction.class, I32GtSInstruction.class, I32GtUInstruction.class,
            /* 0x4C */ I32LeSInstruction.class, I32LeUInstruction.class, I32GeSInstruction.class, I32GeUInstruction.class,

            /* 0x50 */ I64EqzInstruction.class, I64EqInstruction.class, I64NeInstruction.class, I64LtSInstruction.class,
            /* 0x54 */ I64LtUInstruction.class, I64GtSInstruction.class, I64GtUInstruction.class, I64LeSInstruction.class,
            /* 0x58 */ I64LeUInstruction.class, I64GeSInstruction.class, I64GeUInstruction.class, F32EqInstruction.class,
            /* 0x5C */ F32NeInstruction.class, F32LtInstruction.class, F32GtInstruction.class, F32LeInstruction.class,

            /* 0x60 */ F32GeInstruction.class, F64EqInstruction.class, F64NeInstruction.class, F64LtInstruction.class,
            /* 0x64 */ F64GtInstruction.class, F64LeInstruction.class, F64GeInstruction.class, I32ClzInstruction.class,
            /* 0x68 */ I32CtzInstruction.class, I32PopcntInstruction.class, I32AddInstruction.class, I32SubInstruction.class,
            /* 0x6C */ I32MulInstruction.class, I32DivSInstruction.class, I32DivUInstruction.class, I32RemSInstruction.class,

            /* 0x70 */ I32RemUInstruction.class, I32AndInstruction.class, I32OrInstruction.class, I32XorInstruction.class,
            /* 0x74 */ I32ShlInstruction.class, I32ShrSInstruction.class, I32ShrUInstruction.class, I32RotlInstruction.class,
            /* 0x78 */ I32RotrInstruction.class, I64ClzInstruction.class, I64CtzInstruction.class, I64PopcntInstruction.class,
            /* 0x7C */ I64AddInstruction.class, I64SubInstruction.class, I64MulInstruction.class, I64DivSInstruction.class,

            /* 0x80 */ I64DivUInstruction.class, I64RemSInstruction.class, I64RemUInstruction.class, I64AndInstruction.class,
            /* 0x84 */ I64OrInstruction.class, I64XorInstruction.class, I64ShlInstruction.class, I64ShrSInstruction.class,
            /* 0x88 */ I64ShrUInstruction.class, I64RotlInstruction.class, I64RotrInstruction.class, F32AbsInstruction.class,
            /* 0x8C */ F32NegInstruction.class, F32CeilInstruction.class, F32FloorInstruction.class, F32TruncInstruction.class,

            /* 0x90 */ F32NearestInstruction.class, F32SqrtInstruction.class, F32AddInstruction.class, F32SubInstruction.class,
            /* 0x94 */ F32MulInstruction.class, F32DivInstruction.class, F32MinInstruction.class, F32MaxInstruction.class,
            /* 0x98 */ F32CopysignInstruction.class, F64AbsInstruction.class, F64NegInstruction.class, F64CeilInstruction.class,
            /* 0x9C */ F64FloorInstruction.class, F64TruncInstruction.class, F64NearestInstruction.class, F64SqrtInstruction.class,

            /* 0xA0 */ F64AddInstruction.class, F64SubInstruction.class, F64MulInstruction.class, F64DivInstruction.class,
            /* 0xA4 */ F64MinInstruction.class, F64MaxInstruction.class, F64CopysignInstruction.class, I32WrapI64Instruction.class,
            /* 0xA8 */ I32TruncSF32Instruction.class, I32TruncUF32Instruction.class, I32TruncSF64Instruction.class, I32TruncUF64Instruction.class,
            /* 0xAC */ I64ExtendSI32Instruction.class, I64ExtendUI32Instruction.class, I64TruncSF32Instruction.class, I64TruncUF32Instruction.class,

            /* 0xB0 */ I64TruncSF64Instruction.class, I64TruncUF64Instruction.class, F32ConvertSI32Instruction.class, F32ConvertUI32Instruction.class,
            /* 0xB4 */ F32ConvertSI64Instruction.class, F32ConvertUI64Instruction.class, F32DemoteF64Instruction.class, F64ConvertSI32Instruction.class,
            /* 0xB8 */ F64ConvertUI32Instruction.class, F64ConvertSI64Instruction.class, F64ConvertUI64Instruction.class, F64PromoteF32Instruction.class,
            /* 0xBC */ I32ReinterpretF32Instruction.class, I64ReinterpretF64Instruction.class, F32ReinterpretI32Instruction.class, F64ReinterpretI64Instruction.class,

            /* 0xC0 */ null, null, null, null,
            /* 0xC4 */ null, null, null, null,
            /* 0xC8 */ null, null, null, null,
            /* 0xCC */ null, null, null, null,

            /* 0xD0 */ null, null, null, null,
            /* 0xD4 */ null, null, null, null,
            /* 0xD8 */ null, null, null, null,
            /* 0xDC */ null, null, null, null,

            /* 0xE0 */ null, null, null, null,
            /* 0xE4 */ null, null, null, null,
            /* 0xE8 */ null, null, null, null,
            /* 0xEC */ null, null, null, null,

            /* 0xF0 */ null, null, null, null,
            /* 0xF4 */ null, null, null, null,
            /* 0xF8 */ null, null, null, null,
            /* 0xFC */ null, null, null, null
    };

    protected Instruction nextInstruction;
    private int seqIdx;

    public static InstructionSequence decodeSequence(final DataInputStream dataInputStream, final boolean allowElse, final boolean inFunction) throws IOException {
        try {
            Instruction firstInstruction = null;
            Instruction lastInstruction = null;
            int sequenceCount = 0;
            do {
                final int opcode = dataInputStream.readUnsignedByte();
                @SuppressWarnings("unchecked")
                final Class<? extends Instruction> clazz = (Class<? extends Instruction>) decodeTable[opcode];
                if (clazz == null)
                    throw new DecodeException("Unknown opcode " + opcode);
                final Instruction instruction = clazz.newInstance();
                instruction.decode(dataInputStream, inFunction);
                if(firstInstruction == null)
                    firstInstruction = instruction;
                if(lastInstruction != null)
                    lastInstruction.setNextInstruction(instruction);
                instruction.setSeqIdx(sequenceCount);
                lastInstruction = instruction;
                sequenceCount++;
            } while(!(lastInstruction instanceof EndInstruction || (allowElse && lastInstruction instanceof ElseInstruction)));
            return new InstructionSequence(firstInstruction, lastInstruction, sequenceCount);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DecodeException(e);
        }
    }

    public static void validateSequence(final WebAssemblyValidationStack stack, final Context context, final Instruction instructions) throws ValidationException {
        for(Instruction instruction = instructions; instruction != null; instruction = instruction.nextInstruction)
            instruction.validate(stack, context);
    }

    public static void enter(final WebAssemblyVirtualMachine virtualMachine, final Label label, final Instruction instruction) throws TrapException {
        virtualMachine.getStack().push(label);
        virtualMachine.setNextInstruction(instruction);
    }

    public static void exit(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final Deque<WebAssemblyType> deque = new ArrayDeque<>();
        for(;;) {
            final StackElement stackElement = virtualMachine.getStack().pop();
            if(stackElement instanceof WebAssemblyType)
                deque.push((WebAssemblyType) stackElement);
            else if(stackElement instanceof Label) {
                virtualMachine.setNextInstruction(((Label) stackElement).getEnd());
                break;
            } else
                throw new TrapException("Invalid stack element found: " + stackElement.getClass().getCanonicalName());
        }
        while(deque.size() > 0)
            virtualMachine.getStack().push(deque.pop());
    }

    public static void invoke(final WebAssemblyVirtualMachine virtualMachine, final int functionAddress, final Instruction nextInstruction) throws TrapException {
        final FunctionInstance functionInstance = virtualMachine.getStore().getFunctions().get(functionAddress);
        final FunctionType functionType = functionInstance.getType();
        if(functionType.getReturnTypes().size() > 1)
            throw new TrapException("Invalid return types");
        final WebAssemblyType[] locals = new WebAssemblyType[functionType.getParameters().size() + functionInstance.getLocals().size()];
        for(int i = functionType.getParameters().size() - 1; i >= 0; i--)
            locals[i] = virtualMachine.getStack().pop(functionType.getParameters().get(i).getTypeClass());
        for(int i = functionType.getParameters().size(), j = 0; i < locals.length && j < functionInstance.getLocals().size(); i++, j++)
            locals[i] = functionInstance.getLocals().get(j).getZero();
        final Activation frame = new Activation(Arrays.asList(locals), functionInstance.getModule(), functionType.getReturnTypes().size(), nextInstruction, functionAddress);
        final Activation previousFrame = virtualMachine.getCurrentActivation();
        virtualMachine.getStack().push(frame);
        functionInstance.invoke(virtualMachine, previousFrame == null ? null : previousFrame.getModule());
    }

    public static boolean exitFrame(final WebAssemblyVirtualMachine virtualMachine) throws TrapException {
        final Deque<WebAssemblyType> retValues = new ArrayDeque<>();
        final Activation currentFrame = virtualMachine.getCurrentActivation();
        if(currentFrame == null)
            return false;
        for(int i = 0; i < currentFrame.getArity(); i++)
            retValues.push(virtualMachine.getStack().pop(WebAssemblyType.class));
        virtualMachine.getStack().pop(Activation.class);
        while(!retValues.isEmpty())
            virtualMachine.getStack().push(retValues.pop());
        virtualMachine.setNextInstruction(currentFrame.getNextInstruction());
        return true;
    }

    public static boolean isConstantSequence(final Instruction instructions) {
        for(Instruction instruction = instructions; instruction != null; instruction = instruction.nextInstruction)
            if(!instruction.isConstant())
                return false;
        return true;
    }

    protected boolean isConstant() {
        return false;
    }

    public void setNextInstruction(final Instruction nextInstruction) {
        this.nextInstruction = nextInstruction;
    }

    public void setSeqIdx(final int seqIdx) {
        this.seqIdx = seqIdx;
    }

    public abstract void decode(final DataInputStream dataInputStream, final boolean inFunction) throws IOException;
    public abstract void validate(final WebAssemblyValidationStack stack, final Context context) throws ValidationException;
    public abstract void execute(final WebAssemblyVirtualMachine virtualMachine) throws TrapException;

    @Override
    public String toString() {
        return getClass().getName() + '{' + seqIdx + '}';
    }
}
