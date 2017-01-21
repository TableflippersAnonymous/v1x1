package tv.v1x1.common.dto.core;

import com.google.protobuf.InvalidProtocolBufferException;
import tv.v1x1.common.dto.proto.core.ModuleOuterClass;
import tv.v1x1.common.services.cache.CodecCache;
import tv.v1x1.common.services.cache.LambdaCodec;

/**
 * Represents a module that the bot has available to it
 * @author Naomi
 */
public class Module {
    public static final CodecCache.Codec<Module> KEY_CODEC = new LambdaCodec<>(m -> m.getName().getBytes(), b -> new Module(new String(b)));
    public static final CodecCache.Codec<Module> VAL_CODEC = new LambdaCodec<>(m -> m.toProto().toByteArray(), b -> {
        try {
            return Module.fromProto(ModuleOuterClass.Module.parseFrom(b));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    });

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
