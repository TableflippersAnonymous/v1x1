package tv.v1x1.common.dto.core;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.InvalidProtocolBufferException;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;
import tv.v1x1.common.dto.proto.core.UUIDOuterClass;
import tv.v1x1.common.services.cache.CodecCache;
import tv.v1x1.common.services.cache.LambdaCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents a group of {@link Channel}s the bot will treat the same
 * @author Cobi
 */
public class Tenant {
    public static final CodecCache.Codec<Tenant> KEY_CODEC = new LambdaCodec<>(t -> t.getId().toProto().toByteArray(), b -> {
        try {
            return new Tenant(UUID.fromProto(UUIDOuterClass.UUID.parseFrom(b)), null, ImmutableList.of());
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
        final String displayName = proto.getDisplayName();
        final List<ChannelGroup> channelGroups = new ArrayList<>();
        final Tenant tenant = new Tenant(uuid, displayName, channelGroups);
        channelGroups.addAll(proto.getEntriesList().stream().map(entry -> ChannelGroup.fromProto(tenant, entry)).collect(Collectors.toList()));
        return tenant;
    }

    private final UUID id;
    private final String displayName;
    private final List<ChannelGroup> channelGroups;

    public Tenant(final UUID id, final String displayName, final List<ChannelGroup> channelGroups) {
        this.id = id;
        this.displayName = displayName;
        this.channelGroups = channelGroups;
    }

    public UUID getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<ChannelGroup> getChannelGroups() {
        return channelGroups;
    }

    public ChannelOuterClass.Tenant toProto() {
        return ChannelOuterClass.Tenant.newBuilder()
                .setId(id.toProto())
                .setDisplayName(displayName)
                .addAllEntries(channelGroups.stream().map(ChannelGroup::toProtoTenantEntry).collect(Collectors.toList()))
                .build();
    }

    public tv.v1x1.common.dto.db.Tenant toDB() {
        return new tv.v1x1.common.dto.db.Tenant(id.getValue(), displayName);
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
        return "{Tenant/" + id.toString() + ":[" + Joiner.on(",").join(channelGroups) + "]}";
    }

    public Optional<ChannelGroup> getChannelGroup(final Platform platform, final String channelGroupId) {
        return channelGroups.stream()
                .filter(channelGroup -> channelGroup.getPlatform().equals(platform) && channelGroup.getId().equals(channelGroupId))
                .findFirst();
    }

    public Optional<Channel> getChannel(final Platform platform, final String channelGroupId, final String channelId) {
        return getChannelGroup(platform, channelGroupId)
                .flatMap(channelGroup -> channelGroup.getChannel(channelId));
    }

    public List<Channel> getChannels() {
        return getChannelGroups().stream()
                .flatMap(channelGroup -> channelGroup.getChannels().stream())
                .collect(Collectors.toList());
    }
}
