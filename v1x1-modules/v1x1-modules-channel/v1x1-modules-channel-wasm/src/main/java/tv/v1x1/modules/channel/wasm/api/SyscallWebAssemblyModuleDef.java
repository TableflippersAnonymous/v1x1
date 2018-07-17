package tv.v1x1.modules.channel.wasm.api;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.modules.channel.wasm.ExecutionEnvironment;
import tv.v1x1.modules.channel.wasm.vm.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.ModuleInstance;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.store.MemoryInstance;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.lang.invoke.MethodHandles;

public class SyscallWebAssemblyModuleDef extends NativeWebAssemblyModuleDef {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int SYS_BRK = 45;
    private static final int SYS_MMAP_PGOFF = 192;

    private static final I32 ENOSYS = new I32(-38);

    private static final NativeFunctionSpec[] FUNCTIONS = {
            new NativeFunctionSpec("__syscall0", new FunctionType(ImmutableList.of(ValType.I32), ImmutableList.of(ValType.I32)), SyscallWebAssemblyModuleDef::syscall0),
            new NativeFunctionSpec("__syscall1", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), SyscallWebAssemblyModuleDef::syscall1),
            new NativeFunctionSpec("__syscall2", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), SyscallWebAssemblyModuleDef::syscall2),
            new NativeFunctionSpec("__syscall3", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32, ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), SyscallWebAssemblyModuleDef::syscall3),
            new NativeFunctionSpec("__syscall4", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32, ValType.I32, ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), SyscallWebAssemblyModuleDef::syscall4),
            new NativeFunctionSpec("__syscall5", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32, ValType.I32, ValType.I32, ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), SyscallWebAssemblyModuleDef::syscall5),
            new NativeFunctionSpec("__syscall6", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32, ValType.I32, ValType.I32, ValType.I32, ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), SyscallWebAssemblyModuleDef::syscall6)
    };

    public SyscallWebAssemblyModuleDef(final ExecutionEnvironment executionEnvironment) {
        super(executionEnvironment, "syscall", FUNCTIONS);
    }

    private static void syscall0(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final int syscallId = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
        switch(syscallId) {
            default:
                virtualMachine.getStack().push(ENOSYS);
                LOG.info("Unhandled syscall0({})", syscallId);
        }
    }

    private static void syscall1(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final int syscallId = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
        final int param1 = virtualMachine.getCurrentActivation().getLocal(1, I32.class).getVal();
        switch(syscallId) {
            case SYS_BRK:
                virtualMachine.getStack().push(I32.ZERO);
                break;
            default:
                virtualMachine.getStack().push(ENOSYS);
                LOG.info("Unhandled syscall1({}, {})", syscallId, param1);
        }
    }

    private static void syscall2(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final int syscallId = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
        final int param1 = virtualMachine.getCurrentActivation().getLocal(1, I32.class).getVal();
        final int param2 = virtualMachine.getCurrentActivation().getLocal(2, I32.class).getVal();
        virtualMachine.getStack().push(ENOSYS);
        LOG.info("Unhandled syscall2({}, {}, {})", syscallId, param1, param2);
    }

    private static void syscall3(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final int syscallId = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
        final int param1 = virtualMachine.getCurrentActivation().getLocal(1, I32.class).getVal();
        final int param2 = virtualMachine.getCurrentActivation().getLocal(2, I32.class).getVal();
        final int param3 = virtualMachine.getCurrentActivation().getLocal(3, I32.class).getVal();
        virtualMachine.getStack().push(ENOSYS);
        LOG.info("Unhandled syscall3({}, {}, {}, {})", syscallId, param1, param2, param3);
    }

    private static void syscall4(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final int syscallId = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
        final int param1 = virtualMachine.getCurrentActivation().getLocal(1, I32.class).getVal();
        final int param2 = virtualMachine.getCurrentActivation().getLocal(2, I32.class).getVal();
        final int param3 = virtualMachine.getCurrentActivation().getLocal(3, I32.class).getVal();
        final int param4 = virtualMachine.getCurrentActivation().getLocal(4, I32.class).getVal();
        virtualMachine.getStack().push(ENOSYS);
        LOG.info("Unhandled syscall4({}, {}, {}, {}, {})", syscallId, param1, param2, param3, param4);
    }

    private static void syscall5(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final int syscallId = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
        final int param1 = virtualMachine.getCurrentActivation().getLocal(1, I32.class).getVal();
        final int param2 = virtualMachine.getCurrentActivation().getLocal(2, I32.class).getVal();
        final int param3 = virtualMachine.getCurrentActivation().getLocal(3, I32.class).getVal();
        final int param4 = virtualMachine.getCurrentActivation().getLocal(4, I32.class).getVal();
        final int param5 = virtualMachine.getCurrentActivation().getLocal(5, I32.class).getVal();
        virtualMachine.getStack().push(ENOSYS);
        LOG.info("Unhandled syscall5({}, {}, {}, {}, {}, {})", syscallId, param1, param2, param3, param4, param5);
    }

    private static void syscall6(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final int syscallId = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
        final int param1 = virtualMachine.getCurrentActivation().getLocal(1, I32.class).getVal();
        final int param2 = virtualMachine.getCurrentActivation().getLocal(2, I32.class).getVal();
        final int param3 = virtualMachine.getCurrentActivation().getLocal(3, I32.class).getVal();
        final int param4 = virtualMachine.getCurrentActivation().getLocal(4, I32.class).getVal();
        final int param5 = virtualMachine.getCurrentActivation().getLocal(5, I32.class).getVal();
        final int param6 = virtualMachine.getCurrentActivation().getLocal(6, I32.class).getVal();
        switch(syscallId) {
            case SYS_MMAP_PGOFF:
                final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
                final int curPos = memoryInstance.getCurrentPosition();
                if(curPos + param2 > memoryInstance.getData().length) {
                    final int need = curPos + param2 - memoryInstance.getData().length;
                    memoryInstance.grow((need + MemoryInstance.PAGE_SIZE - 1) / MemoryInstance.PAGE_SIZE);
                }
                memoryInstance.setCurrentPosition(memoryInstance.getCurrentPosition() + param2);
                virtualMachine.getStack().push(new I32(curPos));
                break;
            default:
                virtualMachine.getStack().push(ENOSYS);
                LOG.info("Unhandled syscall6({}, {}, {}, {}, {}, {}, {})", syscallId, param1, param2, param3, param4, param5, param6);
        }
    }
}
