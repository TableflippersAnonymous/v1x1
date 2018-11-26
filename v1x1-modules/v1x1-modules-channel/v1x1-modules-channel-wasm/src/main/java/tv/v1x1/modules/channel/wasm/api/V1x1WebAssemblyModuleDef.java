package tv.v1x1.modules.channel.wasm.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Ints;
import org.glassfish.jersey.internal.util.collection.StringKeyIgnoreCaseMultivaluedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.messages.responses.ScheduleResponse;
import tv.v1x1.common.rpc.client.SchedulerServiceClient;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.services.chat.ChatException;
import tv.v1x1.common.services.persistence.KeyValueStore;
import tv.v1x1.common.services.state.NoSuchTargetException;
import tv.v1x1.common.util.data.CompositeKey;
import tv.v1x1.modules.channel.wasm.ExecutionEnvironment;
import tv.v1x1.modules.channel.wasm.vm.runtime.ModuleInstance;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.runtime.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.store.MemoryInstance;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.validation.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.validation.ValType;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class V1x1WebAssemblyModuleDef extends NativeWebAssemblyModuleDef {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int MAX_KV_STORE_SIZE = 256 * 1024 * 1024;

    private static final Map<Integer, Platform> PLATFORM_MAP = new ImmutableMap.Builder<Integer, Platform>()
            .put(1, Platform.TWITCH)
            .put(2, Platform.DISCORD)
            .put(3, Platform.SLACK)
            .put(4, Platform.MIXER)
            .put(5, Platform.YOUTUBE)
            .put(6, Platform.CURSE)
            .put(7, Platform.API)
            .build();

    private static final Map<Integer, String> HTTP_VERBS = new ImmutableMap.Builder<Integer, String>()
            .put(0, "GET")
            .put(1, "POST")
            .put(2, "PUT")
            .put(3, "DELETE")
            .build();

    private static final Map<Integer, DisplayNameType> DISPLAY_NAME_TYPES = new ImmutableMap.Builder<Integer, DisplayNameType>()
            .put(0, DisplayNameType.USER)
            .put(1, DisplayNameType.CHANNEL)
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
            new NativeFunctionSpec("v1x1_log", new FunctionType(ImmutableList.of(ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::log),
            new NativeFunctionSpec("v1x1_tenant_spec_size", new FunctionType(ImmutableList.of(), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::tenantSpecSize),
            new NativeFunctionSpec("v1x1_get_tenant_spec", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::getTenantSpec),
            new NativeFunctionSpec("v1x1_http", new FunctionType(ImmutableList.of(ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::http),
            new NativeFunctionSpec("v1x1_get_display_name", new FunctionType(ImmutableList.of(ValType.I32, ValType.I32, ValType.I32, ValType.I32), ImmutableList.of(ValType.I32)), V1x1WebAssemblyModuleDef::getDisplayName)
    };

    public V1x1WebAssemblyModuleDef(final ExecutionEnvironment executionEnvironment) {
        super(executionEnvironment, "v1x1", FUNCTIONS);
    }

    public enum DisplayNameType {
        USER, CHANNEL
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
        if(baseAddress < 0 || event == null || length < event.length) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        memoryInstance.write(baseAddress, event);
        virtualMachine.getStack().push(new I32(event.length));
        LOG.info("VM Native: read_event({}, {}) = {}", baseAddress, length, event.length);
    }

    private static void sendMessage(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getChatLimiter().tryAcquire()) {
            LOG.warn("VM Message rate limit exceeded.");
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final Channel channel = getChannel(executionEnvironment, virtualMachine, moduleInstance, 0);
        final String message = getString(virtualMachine, moduleInstance, 1);
        if(channel == null || message == null) {
            LOG.warn("VM Message Invalid channel/message: {}/{}", channel, message);
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        try {
            Chat.message(executionEnvironment.getModule(), channel, message);
            virtualMachine.getStack().push(I32.ONE);
            LOG.info("VM Sent Message to {}: {}", channel, message);
        } catch(final IllegalArgumentException e) {
            virtualMachine.getStack().push(I32.ZERO);
            LOG.warn("VM Message to {} errored: {}", channel, message, e);
        }
    }

    private static void purge(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getChatLimiter().tryAcquire()) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final Channel channel = getChannel(executionEnvironment, virtualMachine, moduleInstance, 0);
        final User user = getUser(executionEnvironment, virtualMachine, moduleInstance, 1);
        final int amount = virtualMachine.getCurrentActivation().getLocal(2, I32.class).getVal();
        final String reason = getString(virtualMachine, moduleInstance, 3);
        if(channel == null || user == null || reason == null) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        try {
            Chat.purge(executionEnvironment.getModule(), channel, user, amount, reason);
            virtualMachine.getStack().push(I32.ONE);
        } catch(final IllegalArgumentException e) {
            virtualMachine.getStack().push(I32.ZERO);
        }
    }

    private static void timeout(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getChatLimiter().tryAcquire()) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final Channel channel = getChannel(executionEnvironment, virtualMachine, moduleInstance, 0);
        final User user = getUser(executionEnvironment, virtualMachine, moduleInstance, 1);
        final int length = virtualMachine.getCurrentActivation().getLocal(2, I32.class).getVal();
        final String reason = getString(virtualMachine, moduleInstance, 3);
        if(channel == null || user == null || reason == null) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        try {
            Chat.timeout(executionEnvironment.getModule(), channel, user, length, reason);
            virtualMachine.getStack().push(I32.ONE);
        } catch(final ChatException | IllegalArgumentException e) {
            virtualMachine.getStack().push(I32.ZERO);
        }
    }

    private static void untimeout(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getChatLimiter().tryAcquire()) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final Channel channel = getChannel(executionEnvironment, virtualMachine, moduleInstance, 0);
        final User user = getUser(executionEnvironment, virtualMachine, moduleInstance, 1);
        if(channel == null || user == null) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        try {
            Chat.untimeout(executionEnvironment.getModule(), channel, user);
            virtualMachine.getStack().push(I32.ONE);
        } catch(final ChatException | IllegalArgumentException e) {
            virtualMachine.getStack().push(I32.ZERO);
        }
    }

    private static void kick(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getChatLimiter().tryAcquire()) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final Channel channel = getChannel(executionEnvironment, virtualMachine, moduleInstance, 0);
        final User user = getUser(executionEnvironment, virtualMachine, moduleInstance, 1);
        final String reason = getString(virtualMachine, moduleInstance, 2);
        if(channel == null || user == null || reason == null) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        try {
            Chat.kick(executionEnvironment.getModule(), channel, user, reason);
            virtualMachine.getStack().push(I32.ONE);
        } catch(final ChatException | IllegalArgumentException e) {
            virtualMachine.getStack().push(I32.ZERO);
        }
    }

    private static void ban(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getChatLimiter().tryAcquire()) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final Channel channel = getChannel(executionEnvironment, virtualMachine, moduleInstance, 0);
        final User user = getUser(executionEnvironment, virtualMachine, moduleInstance, 1);
        final int length = virtualMachine.getCurrentActivation().getLocal(2, I32.class).getVal();
        final String reason = getString(virtualMachine, moduleInstance, 3);
        if(channel == null || user == null || reason == null) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        try {
            Chat.ban(executionEnvironment.getModule(), channel, user, length, reason);
            virtualMachine.getStack().push(I32.ONE);
        } catch(final IllegalArgumentException e) {
            virtualMachine.getStack().push(I32.ZERO);
        }
    }

    private static void punish(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getChatLimiter().tryAcquire()) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final Channel channel = getChannel(executionEnvironment, virtualMachine, moduleInstance, 0);
        final User user = getUser(executionEnvironment, virtualMachine, moduleInstance, 1);
        final int length = virtualMachine.getCurrentActivation().getLocal(2, I32.class).getVal();
        final String reason = getString(virtualMachine, moduleInstance, 3);
        if(channel == null || user == null || reason == null) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        try {
            Chat.punish(executionEnvironment.getModule(), channel, user, length, reason);
            virtualMachine.getStack().push(I32.ONE);
        } catch(final IllegalArgumentException e) {
            virtualMachine.getStack().push(I32.ZERO);
        }
    }

    private static void scheduleOnce(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getSchedulerLimiter().tryAcquire()) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final int minutes = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
        final byte[] payload = getBytes(virtualMachine, moduleInstance, 1);
        if(minutes < 0 || minutes > 10080 || payload == null) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final SchedulerServiceClient schedulerServiceClient = executionEnvironment.getModule().getServiceClient(SchedulerServiceClient.class);
        final Future<ScheduleResponse> responseFuture = schedulerServiceClient.scheduleWithDelay(
                minutes * 60000,
                new tv.v1x1.common.dto.core.UUID(UUID.randomUUID()),
                CompositeKey.makeKey(Tenant.KEY_CODEC.encode(executionEnvironment.getTenant()), payload));
        try {
            responseFuture.get();
            virtualMachine.getStack().push(I32.ONE);
        } catch (ExecutionException | InterruptedException e) {
            virtualMachine.getStack().push(I32.ZERO);
        }
    }

    private static void kvstoreWrite(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getKvstoreLimiter().tryAcquire()) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final byte[] key = getBytes(virtualMachine, moduleInstance, 0);
        final byte[] value = getBytes(virtualMachine, moduleInstance, 1);
        if(key == null || value == null) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final KeyValueStore keyValueStore = executionEnvironment.getModule().getPersistentKeyValueStore();
        final byte[] tenant = Tenant.KEY_CODEC.encode(executionEnvironment.getTenant());
        final byte[] compositeKey = CompositeKey.makeKey("VMKVS".getBytes(), tenant, key);
        final byte[] oldValue = keyValueStore.get(compositeKey);
        final int oldLength = oldValue == null ? 0 : oldValue.length;
        if(!changeQuota(keyValueStore, tenant, value.length - oldLength)) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        keyValueStore.put(compositeKey, value);
        virtualMachine.getStack().push(I32.ONE);
    }

    private static void kvstoreHasKey(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getKvstoreLimiter().tryAcquire()) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final byte[] key = getBytes(virtualMachine, moduleInstance, 0);
        if(key == null) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final KeyValueStore keyValueStore = executionEnvironment.getModule().getPersistentKeyValueStore();
        final byte[] tenant = Tenant.KEY_CODEC.encode(executionEnvironment.getTenant());
        final byte[] compositeKey = CompositeKey.makeKey("VMKVS".getBytes(), tenant, key);
        final byte[] value = keyValueStore.get(compositeKey);
        virtualMachine.getStack().push(value == null ? I32.ZERO : I32.ONE);
    }

    private static void kvstoreLength(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getKvstoreLimiter().tryAcquire()) {
            virtualMachine.getStack().push(new I32(-1));
            return;
        }
        final byte[] key = getBytes(virtualMachine, moduleInstance, 0);
        if(key == null) {
            virtualMachine.getStack().push(new I32(-1));
            return;
        }
        final KeyValueStore keyValueStore = executionEnvironment.getModule().getPersistentKeyValueStore();
        final byte[] tenant = Tenant.KEY_CODEC.encode(executionEnvironment.getTenant());
        final byte[] compositeKey = CompositeKey.makeKey("VMKVS".getBytes(), tenant, key);
        final byte[] value = keyValueStore.get(compositeKey);
        virtualMachine.getStack().push(value == null ? new I32(-1) : new I32(value.length));
    }

    private static void kvstoreRead(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getKvstoreLimiter().tryAcquire()) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final byte[] key = getBytes(virtualMachine, moduleInstance, 0);
        if(key == null) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final KeyValueStore keyValueStore = executionEnvironment.getModule().getPersistentKeyValueStore();
        final byte[] tenant = Tenant.KEY_CODEC.encode(executionEnvironment.getTenant());
        final byte[] compositeKey = CompositeKey.makeKey("VMKVS".getBytes(), tenant, key);
        final byte[] value = keyValueStore.get(compositeKey);
        if(!setBytes(virtualMachine, moduleInstance, 1, value)) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        virtualMachine.getStack().push(I32.ONE);
    }

    private static void kvstoreDelete(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getKvstoreLimiter().tryAcquire()) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final byte[] key = getBytes(virtualMachine, moduleInstance, 0);
        if(key == null) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final KeyValueStore keyValueStore = executionEnvironment.getModule().getPersistentKeyValueStore();
        final byte[] tenant = Tenant.KEY_CODEC.encode(executionEnvironment.getTenant());
        final byte[] compositeKey = CompositeKey.makeKey("VMKVS".getBytes(), tenant, key);
        final byte[] oldValue = keyValueStore.get(compositeKey);
        final int oldLength = oldValue == null ? 0 : oldValue.length;
        changeQuota(keyValueStore, tenant, -oldLength);
        keyValueStore.delete(compositeKey);
        virtualMachine.getStack().push(I32.ONE);
    }

    private static void log(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getLogLimiter().tryAcquire()) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final String message = getString(virtualMachine, moduleInstance, 0);
        if(message == null) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        LOG.info("VM Log: {}", message);
        virtualMachine.getStack().push(new I32(message.length()));
    }

    private static void tenantSpecSize(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final byte[] tenant = executionEnvironment.getCurrentEncodedTenant(0);
        virtualMachine.getStack().push(new I32(tenant.length));
    }

    private static void getTenantSpec(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        final int baseAddress = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
        final int length = virtualMachine.getCurrentActivation().getLocal(1, I32.class).getVal();
        final byte[] tenant = executionEnvironment.getCurrentEncodedTenant(baseAddress);
        final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
        if(baseAddress < 0 || tenant == null || length < tenant.length) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        memoryInstance.write(baseAddress, tenant);
        virtualMachine.getStack().push(new I32(tenant.length));
    }

    private static void http(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getHttpLimiter().tryAcquire()) {
            LOG.info("HTTP Ratelimited.");
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        try {
            final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
            final int baseAddress = virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal();
            final int verbInt = decodeI32(memoryInstance, baseAddress);
            final String verb = HTTP_VERBS.get(verbInt);
            if(verb == null) {
                LOG.info("Got unknown HTTP verb {}", verbInt);
                virtualMachine.getStack().push(I32.ZERO);
                return;
            }
            final String uriString = new String(decodeBuffer(memoryInstance, baseAddress + 4));
            LOG.debug("Got URI for HTTP: {}", uriString);
            final URI uri = new URI(uriString);
            final InetAddress[] addresses = InetAddress.getAllByName(uri.getHost());
            final MultivaluedMap<String, Object> headers = getHeaders(memoryInstance, baseAddress + 12);
            final byte[] body = decodeBuffer(memoryInstance, baseAddress + 20);
            final byte[] eventPayload = decodeBuffer(memoryInstance, baseAddress + 28);
            if(Arrays.stream(addresses).anyMatch(InetAddress::isSiteLocalAddress)) {
                LOG.info("HTTP Local blocked.");
                virtualMachine.getStack().push(I32.ZERO);
                return;
            }
            final Client client = ClientBuilder.newClient();
            client.register(new V1x1RequestFilter(executionEnvironment));
            final WebTarget webTarget = client.target(uri);
            final Entity<byte[]> entity;
            if(body.length > 0)
                entity = Entity.entity(body, Optional.ofNullable(headers.getFirst("Content-Type"))
                        .map(Object::toString).orElse(MediaType.APPLICATION_OCTET_STREAM));
            else
                entity = null;
            final Response response = webTarget.request().headers(headers).method(verb, entity);
            executionEnvironment.handleEvent(new HttpResponseEvent(
                    executionEnvironment.getModule().toDto(),
                    response.getStatus(),
                    response.getHeaders(),
                    ByteStreams.toByteArray(response.readEntity(InputStream.class)),
                    eventPayload));
            virtualMachine.getStack().push(I32.ONE);
        } catch(final Exception e) {
            LOG.info("HTTP Error", e);
            virtualMachine.getStack().push(I32.ZERO);
        }
    }

    private static void getDisplayName(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) throws TrapException {
        if(!executionEnvironment.getDisplayNameLimiter().tryAcquire()) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final DisplayNameType displayNameType = DISPLAY_NAME_TYPES.get(virtualMachine.getCurrentActivation().getLocal(0, I32.class).getVal());
        final Platform platform = PLATFORM_MAP.get(virtualMachine.getCurrentActivation().getLocal(1, I32.class).getVal());
        final String id = getString(virtualMachine, moduleInstance, 2);
        if(displayNameType == null || platform == null || id == null || id.length() == 0) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        final String retValue;
        try {
            switch(displayNameType) {
                case USER:
                    retValue = executionEnvironment.getModule().getDisplayNameService().getDisplayNameFromId(platform, id);
                    break;
                case CHANNEL:
                    retValue = executionEnvironment.getModule().getDisplayNameService().getChannelDisplayNameFromId(platform, id);
                    break;
                default:
                    virtualMachine.getStack().push(I32.ZERO);
                    return;
            }
        } catch(final NoSuchTargetException e) {
            virtualMachine.getStack().push(I32.ZERO);
            return;
        }
        setBytes(virtualMachine, moduleInstance, 3, retValue.getBytes());
        virtualMachine.getStack().push(I32.ONE);
    }

    private static Optional<Channel> decodeChannel(final ExecutionEnvironment executionEnvironment, final MemoryInstance memoryInstance, final int baseAddress) throws TrapException {
        final Tenant tenant = executionEnvironment.getTenant();
        final String channelId = new String(decodeBuffer(memoryInstance, baseAddress));
        final String channelGroupId = new String(decodeBuffer(memoryInstance, baseAddress + 16));
        final Platform platform = PLATFORM_MAP.get((int) memoryInstance.readByte(baseAddress + 32));
        LOG.debug("decodeChannel({}, {}, {}, {})", tenant, channelId, channelGroupId, platform);
        return tenant.getChannel(platform, channelGroupId, channelId);
    }

    private static Optional<User> decodeUser(final ExecutionEnvironment executionEnvironment, final MemoryInstance memoryInstance, final int baseAddress) throws TrapException {
        final String userId = new String(decodeBuffer(memoryInstance, baseAddress));
        final Platform platform = PLATFORM_MAP.get((int) memoryInstance.readByte(baseAddress + 8));
        return executionEnvironment.getUser(platform, userId);
    }

    private static byte[] decodeBuffer(final MemoryInstance memoryInstance, final int baseAddress) throws TrapException {
        final int length = decodeI32(memoryInstance, baseAddress);
        final int address = decodeI32(memoryInstance, baseAddress + 4);
        if(length > 128*1024 || length < 0)
            throw new TrapException("Invalid v1x1_buffer size");
        final byte[] data = new byte[length];
        memoryInstance.read(address, data);
        return data;
    }

    private static boolean writeBuffer(final MemoryInstance memoryInstance, final int baseAddress, final byte[] data) throws TrapException {
        final int length = decodeI32(memoryInstance, baseAddress);
        final int address = decodeI32(memoryInstance, baseAddress + 4);
        if(length < data.length)
            return false;
        memoryInstance.write(baseAddress, new I32(data.length).bytes());
        memoryInstance.write(address, data);
        memoryInstance.write(address + data.length, new byte[] {0});
        return true;
    }

    private static Channel getChannel(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance, final int localIdx) throws TrapException {
        final int address = virtualMachine.getCurrentActivation().getLocal(localIdx, I32.class).getVal();
        if(address < 0)
            return null;
        final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
        final Optional<Channel> channel = decodeChannel(executionEnvironment, memoryInstance, address);
        return channel.orElse(null);
    }

    private static User getUser(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance, final int localIdx) throws TrapException {
        final int address = virtualMachine.getCurrentActivation().getLocal(localIdx, I32.class).getVal();
        if(address < 0)
            return null;
        final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
        final Optional<User> user = decodeUser(executionEnvironment, memoryInstance, address);
        return user.orElse(null);
    }

    private static String getString(final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance, final int localIdx) throws TrapException {
        final byte[] bytes = getBytes(virtualMachine, moduleInstance, localIdx);
        if(bytes == null)
            return null;
        return new String(bytes);
    }

    private static byte[] getBytes(final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance, final int localIdx) throws TrapException {
        final int address = virtualMachine.getCurrentActivation().getLocal(localIdx, I32.class).getVal();
        if(address < 0)
            return null;
        final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
        return decodeBuffer(memoryInstance, address);
    }

    private static boolean setBytes(final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance, final int localIdx, final byte[] bytes) throws TrapException {
        final int address = virtualMachine.getCurrentActivation().getLocal(localIdx, I32.class).getVal();
        if(address < 0)
            return false;
        final MemoryInstance memoryInstance = virtualMachine.getStore().getMemories().get(moduleInstance.getMemoryAddresses()[0]);
        return writeBuffer(memoryInstance, address, bytes);
    }

    private static boolean changeQuota(final KeyValueStore keyValueStore, final byte[] tenantBytes, final int delta) {
        final byte[] quotaKey = CompositeKey.makeKey("Quota".getBytes(), tenantBytes);
        final byte[] quotaVal = keyValueStore.get(quotaKey);
        final int quota = quotaVal == null ? 0 : Ints.fromByteArray(quotaVal);
        final int newQuota = quota + delta;
        if(newQuota > MAX_KV_STORE_SIZE) {
            return false;
        }
        keyValueStore.put(quotaKey, Ints.toByteArray(newQuota));
        return true;
    }

    private static int decodeI32(final MemoryInstance memoryInstance, final int address) throws TrapException {
        final byte[] bytes = new byte[4];
        memoryInstance.read(address, bytes);
        return I32.decode(I32.swapEndian(bytes)).getVal();
    }

    private static MultivaluedMap<String, Object> getHeaders(final MemoryInstance memoryInstance, final int baseAddress) throws TrapException {
        final MultivaluedMap<String, Object> headers = new StringKeyIgnoreCaseMultivaluedMap<>();
        final int count = decodeI32(memoryInstance, baseAddress);
        final int headerBase = decodeI32(memoryInstance, baseAddress + 4);
        for(int i = 0; i < count; i++) {
            final String name = new String(decodeBuffer(memoryInstance, headerBase));
            final String value = new String(decodeBuffer(memoryInstance, headerBase + 8));
            headers.add(name, value);
        }
        return headers;
    }
}
