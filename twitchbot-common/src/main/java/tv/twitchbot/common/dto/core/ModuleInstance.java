package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ModuleOuterClass;

/**
 * Represents a particular running process of a {@link Module}
 * @author Naomi
 */
public class ModuleInstance {
    public static ModuleInstance fromProto(ModuleOuterClass.ModuleInstance moduleInstance) {
        Module module = Module.fromProto(moduleInstance.getModule());
        UUID uuid = UUID.fromProto(moduleInstance.getId());
        return new ModuleInstance(uuid, module);
    }

    private UUID id;
    private Module module;

    public ModuleInstance(UUID id, Module module) {
        this.id = id;
        this.module = module;
    }

    public UUID getId() {
        return id;
    }

    public Module getModule() {
        return module;
    }

    public ModuleOuterClass.ModuleInstance toProto() {
        return ModuleOuterClass.ModuleInstance.newBuilder()
                .setId(id.toProto())
                .setModule(module.toProto())
                .build();
    }
}
