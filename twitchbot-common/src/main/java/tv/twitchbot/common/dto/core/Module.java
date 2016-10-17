package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ModuleOuterClass;

/**
 * Represents a module that the bot has available to it
 * @author Naomi
 */
public class Module {
    public static Module fromProto(ModuleOuterClass.Module module) {
        return new Module(module.getName());
    }

    private String name;

    public Module(String name) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Module module = (Module) o;

        if (name != null ? !name.equals(module.name) : module.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
