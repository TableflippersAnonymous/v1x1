package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ModuleOuterClass;

/**
 * Represents a module that the bot has available to it
 * @author Cobi
 */
public class Module {
    public static Module fromProto(final ModuleOuterClass.Module module) {
        return new Module(module.getName());
    }

    private final String name;

    public Module(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ModuleOuterClass.Module toProto() {
        return ModuleOuterClass.Module.newBuilder()
                .setName(name)
                .build();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Module module = (Module) o;

        return name != null ? name.equals(module.name) : module.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
