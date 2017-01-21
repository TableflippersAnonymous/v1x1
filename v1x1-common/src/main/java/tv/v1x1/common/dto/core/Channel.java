package tv.v1x1.common.dto.core;

import com.google.common.primitives.Ints;
import com.google.protobuf.InvalidProtocolBufferException;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;
import tv.v1x1.common.dto.proto.core.ModuleOuterClass;
import tv.v1x1.common.services.cache.CodecCache;
import tv.v1x1.common.services.cache.LambdaCodec;
import tv.v1x1.common.util.data.CompositeKey;

/**
 * Represents a single text chat room on a platform on which the bot is a part of
 * @author Cobi
 */
public abstract class Channel {
    public static final CodecCache.Codec<Channel> KEY_CODEC = new LambdaCodec<>(c -> CompositeKey.makeKey(Tenant.KEY_CODEC.encode(c.getTenant()), Ints.toByteArray(c.getPlatform().ordinal()), c.getId().getBytes(), c.getDisplayName().getBytes()), b -> {
        final byte[][] keys = CompositeKey.getKeys(b);
        final Tenant tenant = Tenant.KEY_CODEC.decode(keys[0]);
        final Platform platform = Platform.values()[Ints.fromByteArray(keys[1])];
        final String id = new String(keys[2]);
        final String displayName = new String(keys[3]);
        switch(platform) {
            case TWITCH: return new TwitchChannel(id, tenant, displayName);
            case DISCORD: return new DiscordChannel(id, tenant, displayName);
            default: throw new IllegalStateException("Unknown channel platform " + platform.name());
        }
    });
    public static final CodecCache.Codec<Channel> VAL_CODEC = new LambdaCodec<>(c -> c.toProto().toByteArray(), b -> {
        try {
            return Channel.fromProto(ChannelOuterClass.Channel.parseFrom(b));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    });

    public static Channel fromProto(final ChannelOuterClass.Channel channel) {
        switch(channel.getPlatform()) {
            case TWITCH: return TwitchChannel.fromProto(channel);
            case DISCORD: return DiscordChannel.fromProto(channel);
            default: throw new IllegalStateException("Unknown channel platform " + channel.getPlatform().name());
        }
    }

    public static Channel fromProto(final Tenant tenant, final ChannelOuterClass.TenantEntry tenantEntry) {
        switch(tenantEntry.getPlatform()) {
            case TWITCH: return TwitchChannel.fromProto(tenant, tenantEntry);
            case DISCORD: return DiscordChannel.fromProto(tenant, tenantEntry);
            default: throw new IllegalStateException("Unknown channel platform " + tenantEntry.getPlatform().name());
        }
    }

    protected String id;
    protected Tenant tenant;
    protected String displayName;

    public Channel(final String id, final Tenant tenant, final String displayName) {
        this.id = id;
        this.tenant = tenant;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Tenant getTenant() {
        return tenant;
    }

    @Override
    public String toString() {
        return displayName + "{" + id + "/" + getClass().getSimpleName() + "}";
    }

    public abstract Platform getPlatform();

    public abstract ChannelOuterClass.Channel toProto();
    public abstract ChannelOuterClass.TenantEntry toProtoTenantEntry();

    public abstract tv.v1x1.common.dto.db.Channel toDB();
    public abstract tv.v1x1.common.dto.db.Tenant.Entry toDBTenant();
}
