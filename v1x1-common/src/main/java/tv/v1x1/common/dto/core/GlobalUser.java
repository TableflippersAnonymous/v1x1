package tv.v1x1.common.dto.core;

import com.google.common.collect.ImmutableList;
import com.google.protobuf.InvalidProtocolBufferException;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.proto.core.UUIDOuterClass;
import tv.v1x1.common.dto.proto.core.UserOuterClass;
import tv.v1x1.common.services.cache.CodecCache;
import tv.v1x1.common.services.cache.LambdaCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents a group of {@link User}s that we may treat the same across all platforms
 * (Think: Linking your Discord account to Twitch)
 * @author Cobi
 */
public class GlobalUser {
    public static final CodecCache.Codec<GlobalUser> KEY_CODEC = new LambdaCodec<>(u -> u.getId().toProto().toByteArray(), b -> {
        try {
            return new GlobalUser(UUID.fromProto(UUIDOuterClass.UUID.parseFrom(b)), ImmutableList.of());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    });
    public static final CodecCache.Codec<GlobalUser> VAL_CODEC = new LambdaCodec<>(u -> u.toProto().toByteArray(), b -> {
        try {
            return GlobalUser.fromProto(UserOuterClass.GlobalUser.parseFrom(b));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    });

    public static GlobalUser fromProto(final UserOuterClass.GlobalUser proto) {
        final List<User> list = new ArrayList<>();
        final UUID uuid = UUID.fromProto(proto.getId());
        final GlobalUser globalUser = new GlobalUser(uuid, list);
        list.addAll(proto.getEntriesList().stream().map(entry -> User.fromProto(globalUser, entry)).collect(Collectors.toList()));
        return globalUser;
    }

    private final UUID id;
    private final List<User> entries;

    public GlobalUser(final UUID id, final List<User> entries) {
        this.id = id;
        this.entries = entries;
    }

    public UUID getId() {
        return id;
    }

    public List<User> getEntries() {
        return entries;
    }

    public UserOuterClass.GlobalUser toProto() {
        return UserOuterClass.GlobalUser.newBuilder()
                .setId(id.toProto())
                .addAllEntries(entries.stream().map(User::toProtoGlobalUserEntry).collect(Collectors.toList()))
                .build();
    }

    public tv.v1x1.common.dto.db.GlobalUser toDB() {
        return new tv.v1x1.common.dto.db.GlobalUser(id.getValue(), entries.stream().map(User::toDBGlobalUser).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GlobalUser that = (GlobalUser) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{GlobalUser/" + getId() + "}";
    }

    public Optional<User> getUser(final Platform platform, final String userId) {
        return entries.stream().filter(user -> user.getPlatform().equals(platform) && user.getId().equals(userId)).findFirst();
    }
}
