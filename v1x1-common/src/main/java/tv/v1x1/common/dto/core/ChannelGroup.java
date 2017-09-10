package tv.v1x1.common.dto.core;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import com.google.protobuf.InvalidProtocolBufferException;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;
import tv.v1x1.common.services.cache.CodecCache;
import tv.v1x1.common.services.cache.LambdaCodec;
import tv.v1x1.common.util.data.CompositeKey;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/26/2017.
 */
public abstract class ChannelGroup {
    public static final CodecCache.Codec<ChannelGroup> KEY_CODEC = new LambdaCodec<>(c -> CompositeKey.makeKey(
            Tenant.KEY_CODEC.encode(c.getTenant()), Ints.toByteArray(c.getPlatform().ordinal()), c.getId().getBytes(),
            c.getDisplayName().getBytes()
    ), b -> {
        final byte[][] keys = CompositeKey.getKeys(b);
        final Tenant tenant = Tenant.KEY_CODEC.decode(keys[0]);
        final Platform platform = Platform.values()[Ints.fromByteArray(keys[1])];
        final String id = new String(keys[2]);
        final String displayName = new String(keys[3]);
        switch(platform) {
            case TWITCH: return new TwitchChannelGroup(id, tenant, displayName, ImmutableList.of());
            case DISCORD: return new DiscordGuild(id, tenant, displayName, ImmutableList.of());
            default: throw new IllegalStateException("Unknown channel platform " + platform.name());
        }
    });

    public static final CodecCache.Codec<ChannelGroup> VAL_CODEC = new LambdaCodec<>(c -> c.toProto().toByteArray(), b -> {
        try {
            return ChannelGroup.fromProto(ChannelOuterClass.ChannelGroup.parseFrom(b));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    });

    public static ChannelGroup fromProto(final ChannelOuterClass.ChannelGroup channelGroup) {
        switch(channelGroup.getPlatform()) {
            case TWITCH: return TwitchChannelGroup.fromProto(channelGroup);
            case DISCORD: return DiscordGuild.fromProto(channelGroup);
            default: throw new IllegalStateException("Unknown channel platform " + channelGroup.getPlatform().name());
        }
    }

    public static ChannelGroup fromProto(final Tenant tenant, final ChannelOuterClass.TenantEntry tenantEntry) {
        switch(tenantEntry.getPlatform()) {
            case TWITCH: return TwitchChannelGroup.fromProto(tenant, tenantEntry);
            case DISCORD: return DiscordGuild.fromProto(tenant, tenantEntry);
            default: throw new IllegalStateException("Unknown channel platform " + tenantEntry.getPlatform().name());
        }
    }

    protected String id;
    protected Tenant tenant;
    protected String displayName;
    protected List<Channel> channels;

    public ChannelGroup(final String id, final Tenant tenant, final String displayName, final List<Channel> channels) {
        this.id = id;
        this.tenant = tenant;
        this.displayName = displayName;
        this.channels = channels;
    }

    public String getId() {
        return id;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public tv.v1x1.common.dto.db.ChannelGroup toDB() {
        return new tv.v1x1.common.dto.db.ChannelGroup(getPlatform(), id, displayName, tenant.getId().getValue());
    }

    public ChannelOuterClass.ChannelGroup toProto() {
        return ChannelOuterClass.ChannelGroup.newBuilder()
                .setId(id)
                .setDisplayName(displayName)
                .setTenant(tenant.toProto())
                .setPlatform(getPlatform().toProto())
                .addAllEntries(getChannels().stream().map(Channel::toProtoChannelGroupEntry).collect(Collectors.toList()))
                .build();
    }

    public ChannelOuterClass.TenantEntry toProtoTenantEntry() {
        return ChannelOuterClass.TenantEntry.newBuilder()
                .setId(id)
                .setDisplayName(displayName)
                .setPlatform(getPlatform().toProto())
                .addAllEntries(getChannels().stream().map(Channel::toProtoChannelGroupEntry).collect(Collectors.toList()))
                .build();
    }

    public Optional<Channel> getChannel(final String channelId) {
        return channels.stream()
                .filter(channel -> channel.getId().equals(channelId))
                .findFirst();
    }

    @Override
    public String toString() {
        return "{ChannelGroup/" + getPlatform().toString() + ":" + id +
                ":" + displayName + ":[" +
                Joiner.on(",").join(channels) +
                "]}";
    }

    public abstract Platform getPlatform();
}
