package tv.v1x1.modules.channel.wasm.api;

import com.google.common.collect.ImmutableList;
import tv.v1x1.modules.channel.wasm.ExecutionEnvironment;
import tv.v1x1.modules.channel.wasm.vm.FunctionType;
import tv.v1x1.modules.channel.wasm.vm.ModuleInstance;
import tv.v1x1.modules.channel.wasm.vm.TrapException;
import tv.v1x1.modules.channel.wasm.vm.ValType;
import tv.v1x1.modules.channel.wasm.vm.WebAssemblyVirtualMachine;

public class V1x1WebAssemblyModuleDef extends NativeWebAssemblyModuleDef {
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

    private static void eventSize(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void readEvent(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void sendMessage(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
    }

    private static void purge(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance moduleInstance) {
        /* TODO */
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

    private static void log(final ExecutionEnvironment executionEnvironment, final WebAssemblyVirtualMachine virtualMachine, final ModuleInstance previousModule) throws TrapException {
        /* TODO */
    }
}
