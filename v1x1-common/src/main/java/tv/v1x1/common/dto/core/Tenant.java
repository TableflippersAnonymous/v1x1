package tv.v1x1.common.dto.core;

import com.google.common.collect.ImmutableList;
import com.google.protobuf.InvalidProtocolBufferException;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;
import tv.v1x1.common.dto.proto.core.UUIDOuterClass;
import tv.v1x1.common.services.cache.CodecCache;
import tv.v1x1.common.services.cache.LambdaCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a group of {@link Channel}s the bot will treat the same
 * @author Cobi
 */
public class Tenant {
    public static final CodecCache.Codec<Tenant> KEY_CODEC = new LambdaCodec<>(t -> t.getId().toProto().toByteArray(), b -> {
        try {
            return new Tenant(UUID.fromProto(UUIDOuterClass.UUID.parseFrom(b)), ImmutableList.of());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    });
    public static final CodecCache.Codec<Tenant> VAL_CODEC = new LambdaCodec<>(t -> t.toProto().toByteArray(), b -> {
        try {
            return Tenant.fromProto(ChannelOuterClass.Tenant.parseFrom(b));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    });

    public static Tenant fromProto(final ChannelOuterClass.Tenant proto) {
        final UUID uuid = UUID.fromProto(proto.getId());
        final List<Channel> channels = new ArrayList<>();
        final Tenant tenant = new Tenant(uuid, channels);
        channels.addAll(proto.getEntriesList().stream().map(entry -> Channel.fromProto(tenant, entry)).collect(Collectors.toList()));
        return tenant;
    }

    private final UUID id;
    private final List<Channel> channels;

    public Tenant(final UUID id, final List<Channel> channels) {
        this.id = id;
        this.channels = channels;
    }

    public UUID getId() {
        return id;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public ChannelOuterClass.Tenant toProto() {
        return ChannelOuterClass.Tenant.newBuilder()
                .setId(id.toProto())
                .addAllEntries(channels.stream().map(Channel::toProtoTenantEntry).collect(Collectors.toList()))
                .build();
    }

    public tv.v1x1.common.dto.db.Tenant toDB() {
        return new tv.v1x1.common.dto.db.Tenant(id.getValue(), channels.stream().map(Channel::toDBTenant).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Tenant tenant = (Tenant) o;

        return id != null ? id.equals(tenant.id) : tenant.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{Tenant/" + id.toString() + "}";
    }
}
