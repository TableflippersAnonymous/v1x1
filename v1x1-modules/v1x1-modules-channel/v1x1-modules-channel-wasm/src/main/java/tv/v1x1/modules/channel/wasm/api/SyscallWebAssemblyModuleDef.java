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
import tv.v1x1.modules.channel.wasm.vm.store.MemoryPage;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.lang.invoke.MethodHandles;
import java.util.Date;

public class SyscallWebAssemblyModuleDef extends NativeWebAssemblyModuleDef {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int SYS_EXIT = 1;
    private static final int SYS_TIME = 13;
    private static final int SYS_BRK = 45;
    private static final int SYS_GETTIMEOFDAY = 78;
    private static final int SYS_MUNMAP = 91;
    private static final int SYS_MMAP_PGOFF = 192;

    private static final int EPERM  =  1;
    private static final int ENOMEM = 12;
    private static final int EACCES = 13;
    private static final int EEXIST = 17;
    private static final int EINVAL = 22;

    private static final int PROT_READ      = 0x1;
    private static final int PROT_WRITE     = 0x2;
    private static final int PROT_EXEC      = 0x4;

    private static final int MAP_FIXED      =    0x10;
    private static final int MAP_ANONYMOUS  =    0x20;
    private static final int MAP_FIXED_NOREPLACE = 0x100000;

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
        LOG.info("syscall1({}, {})", syscallId, param1);
        switch(syscallId) {
            case SYS_EXIT:
                throw new TrapException("Exit: " + param1);
            case SYS_TIME:
                final I32 time = new I32((int) (new Date().getTime() / 1000));
                if(param1 != 0) {
                    final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
                    memoryInstance.write(param1, time.bytes());
                }
                virtualMachine.getStack().push(time);
                break;
            case SYS_BRK:
                final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
                brk(param1, memoryInstance);
                virtualMachine.getStack().push(new I32(memoryInstance.getCurrentBreak()));
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
        LOG.info("syscall2({}, {}, {})", syscallId, param1, param2);
        switch(syscallId) {
            case SYS_GETTIMEOFDAY:
                if(param1 != 0 || param2 != 0) {
                    final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
                    if(param1 != 0) {
                        final long currentTimeMillis = new Date().getTime();
                        final I32 seconds = new I32((int) (currentTimeMillis / 1000));
                        final I32 micros = new I32((int) ((currentTimeMillis % 1000) * 1000));
                        memoryInstance.write(param1, seconds.bytes());
                        memoryInstance.write(param1 + 4, micros.bytes());
                    }
                    if(param2 != 0) {
                        final I32 minutesWest = I32.ZERO;
                        final I32 dstTime = I32.ZERO;
                        memoryInstance.write(param2, minutesWest.bytes());
                        memoryInstance.write(param2 + 4, dstTime.bytes());
                    }
                }
                virtualMachine.getStack().push(I32.ZERO);
                break;
            case SYS_MUNMAP:
                final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
                virtualMachine.getStack().push(new I32(munmap(memoryInstance, param1, param2)));
                break;
            default:
                virtualMachine.getStack().push(ENOSYS);
                LOG.info("Unhandled syscall2({}, {}, {})", syscallId, param1, param2);
        }
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
        LOG.info("syscall6({}, {}, {}, {}, {}, {}, {})", syscallId, param1, param2, param3, param4, param5, param6);
        switch(syscallId) {
            case SYS_MMAP_PGOFF:
                final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
                virtualMachine.getStack().push(new I32(mmap(memoryInstance, param1, param2, param3, param4, param5, param6)));
                break;
            default:
                virtualMachine.getStack().push(ENOSYS);
                LOG.info("Unhandled syscall6({}, {}, {}, {}, {}, {}, {})", syscallId, param1, param2, param3, param4, param5, param6);
        }
    }

    private static int mmap(final MemoryInstance memoryInstance, final int address, final int length, final int protection, final int flags, final int fd, final int offset) {
        if(length == 0)
            return -EINVAL;
        if((flags & MAP_ANONYMOUS) == 0)
            return -EACCES;
        if((protection & PROT_EXEC) != 0)
            return -EPERM;
        final int pageCount = (length >> 16) + 1;
        if(memoryInstance.getPageCount() + pageCount > MemoryInstance.MAX_SIZE)
            return -ENOMEM;
        final MemoryPage[] pages = new MemoryPage[pageCount];
        for(int i = 0; i < pageCount; i++)
            pages[i] = new MemoryPage((protection & PROT_READ) != 0, (protection & PROT_WRITE) != 0);
        if((flags & MAP_FIXED_NOREPLACE) != 0) {
            if((address & 0xffff) != 0)
                return -EINVAL;
            final int retAddress = memoryInstance.mapAt(address >> 16, pages);
            if(retAddress == -2)
                return -EEXIST;
            if(retAddress < 0)
                return -ENOMEM;
            return retAddress;
        }
        if((flags & MAP_FIXED) != 0) {
            if((address & 0xffff) != 0)
                return -EINVAL;
            if((address >> 16) < memoryInstance.getBreakPages())
                return -EINVAL;
            final int retAddress = memoryInstance.forceMapAt(address >> 16, pages);
            if(retAddress < 0)
                return -ENOMEM;
            return retAddress;
        }
        if(address == 0) {
            final int retAddress = memoryInstance.map(pages);
            if(retAddress < 0)
                return -ENOMEM;
            return retAddress;
        }
        final int retAddress = memoryInstance.tryMapAt(address >> 16, pages);
        if(retAddress < 0)
            return -ENOMEM;
        return retAddress;
    }

    private static int munmap(final MemoryInstance memoryInstance, final int address, final int length) {
        if((address & 0xffff) != 0)
            return -EINVAL;
        memoryInstance.unmap(address >> 16, (length >> 16) + 1);
        return 0;
    }

    private static int brk(final int breakAddress, final MemoryInstance memoryInstance) {
        final int curPos = memoryInstance.getCurrentBreak();
        final int breakPage = breakAddress >> 16;
        if(breakPage >= memoryInstance.getBreakPages()) {
            final int need = breakPage - memoryInstance.getBreakPages() + 1;
            memoryInstance.grow(need);
        }
        if(breakAddress > curPos)
            memoryInstance.setCurrentBreak(breakAddress);
        return curPos;
    }
}
