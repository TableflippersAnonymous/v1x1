package tv.v1x1.common.dto.core;

import com.google.protobuf.MessageLite;
import tv.v1x1.common.dto.proto.core.ModuleOuterClass;

/**
 * Represents a particular running process of a {@link Module}
 * @author Cobi
 */
public class ModuleInstance {
    public static ModuleInstance fromProto(final ModuleOuterClass.ModuleInstance moduleInstance) {
        final Module module = Module.fromProto(moduleInstance.getModule());
        final UUID uuid = UUID.fromProto(moduleInstance.getId());
        return new ModuleInstance(uuid, module);
    }

    private final UUID id;
    private final Module module;

    public ModuleInstance(final UUID id, final Module module) {
        this.id = id;
        this.module = module;
    }

    public UUID getId() {
        return id;
    }

    public Module getModule() {
        return module;
    }

    public MessageLite toProto() {
        return ModuleOuterClass.ModuleInstance.newBuilder()
                .setId(id.toProto())
                .setModule(module.toProto())
                .build();
    }
}
