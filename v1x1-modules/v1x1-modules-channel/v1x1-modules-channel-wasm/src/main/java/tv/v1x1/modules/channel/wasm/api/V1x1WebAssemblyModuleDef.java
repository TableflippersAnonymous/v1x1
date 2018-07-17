package tv.v1x1.modules.channel.wasm.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.modules.channel.wasm.ExecutionEnvironment;
import tv.v1x1.modules.channel.wasm.vm.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.ModuleInstance;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.store.MemoryInstance;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;

public class V1x1WebAssemblyModuleDef extends NativeWebAssemblyModuleDef {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Map<Integer, Platform> PLATFORM_MAP = new ImmutableMap.Builder<Integer, Platform>()
            .put(1, Platform.TWITCH)
            .put(2, Platform.DISCORD)
            .put(3, Platform.SLACK)
            .put(4, Platform.MIXER)
            .put(5, Platform.YOUTUBE)
            .put(6, Platform.CURSE)
            .put(7, Platform.API)
            .build();

    private static final NativeFunctionSpec[] FUNCTIONS = {
            new NativeFunctionSpec("v1x1_event_size", new FunctionType(ImmutableList.of(), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::eventSize),
            new NativeFunctionSpec("v1x1_read_event", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::readEvent),
            new NativeFunctionSpec("v1x1_send_message", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::sendMessage),
            new NativeFunctionSpec("v1x1_purge", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32, ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::purge),
            new NativeFunctionSpec("v1x1_timeout", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32, ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::timeout),
            new NativeFunctionSpec("v1x1_untimeout", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::untimeout),
            new NativeFunctionSpec("v1x1_kick", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::kick),
            new NativeFunctionSpec("v1x1_ban", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32, ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::ban),
            new NativeFunctionSpec("v1x1_punish", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32, ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::punish),
            new NativeFunctionSpec("v1x1_schedule_once", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::scheduleOnce),
            new NativeFunctionSpec("v1x1_kvstore_write", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::kvstoreWrite),
            new NativeFunctionSpec("v1x1_kvstore_has_key", new FunctionType(ImmutableList.of(ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::kvstoreHasKey),
            new NativeFunctionSpec("v1x1_kvstore_length", new FunctionType(ImmutableList.of(ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::kvstoreLength),
            new NativeFunctionSpec("v1x1_kvstore_read", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::kvstoreRead),
            new NativeFunctionSpec("v1x1_kvstore_delete", new FunctionType(ImmutableList.of(ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::kvstoreDelete),
            new NativeFunctionSpec("v1x1_log", new FunctionType(ImmutableList.of(ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::log)
    };

    public V1x1WebAssemblyModuleDef(final ExecutionEnvironment executionEnvironment) {
        super(executionEnvironment, "v1x1", FUNCTIONS);
    }

    private static void eventSize(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final byte[] event = executionEnvironment.getCurrentEvent(0);
        virtualMachine.getStack().push(new I32(event != null ? event.length : -1));
        LOG.info("VM Native: event_size() = {}", event != null ? event.length : -1);
    }

    private static void readEvent(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final int baseAddress = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
        final int length = virtualMachine.getCurrentActivation().getLocal(1, I32.class).getVal();
        final byte[] event = executionEnvironment.getCurrentEvent(baseAddress);
        final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
        if(baseAddress < 0 || event == null || length < event.length || length + baseAddress > memoryInstance.getData().length) {
            virtualMachine.getStack().push(new I32(0));
            LOG.info("VM Native: read_event({}, {}) = 0", baseAddress, length);
            return;
        }
        System.arraycopy(event, 0, memoryInstance.getData(), baseAddress, event.length);
        virtualMachine.getStack().push(new I32(event.length));
        LOG.info("VM Native: read_event({}, {}) = {}", baseAddress, length, event.length);
    }

    private static void sendMessage(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final int channelAddress = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
        final int messageBufferAddress = virtualMachine.getCurrentActivation().getLocal(1, I32.class).getVal();
        final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
        if(channelAddress < 0 || messageBufferAddress < 0) {
            virtualMachine.getStack().push(new I32(0));
            LOG.info("VM Native: send_message({}, {}) = 0", channelAddress, messageBufferAddress);
            return;
        }
        final Optional<Channel> channel = decodeChannel(executionEnvironment, memoryInstance, channelAddress);
        final String message = new String(decodeBuffer(memoryInstance, messageBufferAddress));
        if(!channel.isPresent()) {
            virtualMachine.getStack().push(new I32(0));
            LOG.info("VM Native: send_message({}, {}) = 0", channelAddress, messageBufferAddress);
            return;
        }
        try {
            Chat.message(executionEnvironment.getModule(), channel.get(), message);
            virtualMachine.getStack().push(new I32(message.length()));
            LOG.info("VM Native: send_message({} = {}, {} = {}) = {}", channelAddress, channel, messageBufferAddress, message, message.length());
        } catch(final IllegalArgumentException e) {
            virtualMachine.getStack().push(new I32(0));
            LOG.info("VM Native: send_message({} = {}, {} = {}) = 0", channelAddress, channel, messageBufferAddress, message);
        }
    }

    private static void purge(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final int channelAddress = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
        final int userAddress = virtualMachine.getCurrentActivation().getLocal(1, I32.class).getVal();
        final int amount = virtualMachine.getCurrentActivation().getLocal(2, I32.class).getVal();
        final int reasonAddress = virtualMachine.getCurrentActivation().getLocal(3, I32.class).getVal();
        final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
        if(channelAddress < 0 || userAddress < 0 || reasonAddress < 0) {
            virtualMachine.getStack().push(new I32(0));
            return;
        }
        final Optional<Channel> channel = decodeChannel(executionEnvironment, memoryInstance, channelAddress);
        final Optional<User> user = decodeUser(executionEnvironment, memoryInstance, userAddress);
        final String reason = new String(decodeBuffer(memoryInstance, reasonAddress));
        if(!channel.isPresent() || !user.isPresent()) {
            virtualMachine.getStack().push(new I32(0));
            return;
        }
        try {
            Chat.purge(executionEnvironment.getModule(), channel.get(), user.get(), amount, reason);
            virtualMachine.getStack().push(new I32(reason.length()));
        } catch(final IllegalArgumentException e) {
            virtualMachine.getStack().push(new I32(0));
        }
    }

    private static void timeout(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void untimeout(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void kick(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void ban(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void punish(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void scheduleOnce(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void kvstoreWrite(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void kvstoreHasKey(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void kvstoreLength(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void kvstoreRead(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void kvstoreDelete(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void log(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final int messageBufferAddress = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
        LOG.info("VM Native: log({}) = 0", messageBufferAddress);
        final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
        if(messageBufferAddress < 0) {
            virtualMachine.getStack().push(new I32(0));
            LOG.info("VM Native: log({}) = 0", messageBufferAddress);
            return;
        }
        final String message = new String(decodeBuffer(memoryInstance, messageBufferAddress));
        LOG.info("VM Log: {}", message);
        virtualMachine.getStack().push(new I32(message.length()));
    }

    private static Optional<Channel> decodeChannel(final ExecutionEnvironment executionEnvironment, final MemoryInstance memoryInstance, final int baseAddress) throws TrapException {
        final Tenant tenant = executionEnvironment.getTenant();
        final String channelId = new String(decodeBuffer(memoryInstance, baseAddress));
        final String channelGroupId = new String(decodeBuffer(memoryInstance, baseAddress + 16));
        final Platform platform = PLATFORM_MAP.get((int) memoryInstance.getData()[baseAddress + 32]);
        return tenant.getChannel(platform, channelGroupId, channelId);
    }

    private static Optional<User> decodeUser(final ExecutionEnvironment executionEnvironment, final MemoryInstance memoryInstance, final int baseAddress) throws TrapException {
        final String userId = new String(decodeBuffer(memoryInstance, baseAddress));
        final Platform platform = PLATFORM_MAP.get((int) memoryInstance.getData()[baseAddress + 8]);
        return executionEnvironment.getUser(platform, userId);
    }

    private static byte[] decodeBuffer(final MemoryInstance memoryInstance, final int baseAddress) throws TrapException {
        final byte[] lengthArray = new byte[4];
        final byte[] addressArray = new byte[4];
        System.arraycopy(memoryInstance.getData(), baseAddress, lengthArray, 0, 4);
        System.arraycopy(memoryInstance.getData(), baseAddress + 4, addressArray, 0, 4);
        final int length = I32.decode(I32.swapEndian(lengthArray)).getVal();
        final int address = I32.decode(I32.swapEndian(addressArray)).getVal();
        LOG.info("decodeBuffer({} => {}/{} => {}/{})", baseAddress, lengthArray, length, addressArray, address);
        if(address + length > memoryInstance.getData().length || length > 1024*1024 || length < 0)
            throw new TrapException("Invalid v1x1_buffer size");
        final byte[] data = new byte[length];
        System.arraycopy(memoryInstance.getData(), address, data, 0, length);
        LOG.info("decodeBuffer() = {}", data);
        return data;
    }
}
