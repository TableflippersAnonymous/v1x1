package tv.v1x1.modules.channel.wasm.vm;

import tv.v1x1.modules.channel.wasm.vm.instructions.NopInstruction;
import tv.v1x1.modules.channel.wasm.vm.instructions.UnreachableInstruction;

public abstract class Instruction {
    /* See https://webassembly.github.io/spec/core/appendix/index-instructions.html */
    private final static Class<?>[] decodeTable = {
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

    public abstract boolean validate(final Context context);
    public abstract void execute(final Context context) throws TrapException;
}
