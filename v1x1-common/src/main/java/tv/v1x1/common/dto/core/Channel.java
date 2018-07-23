package tv.v1x1.common.dto.core;

import com.google.protobuf.InvalidProtocolBufferException;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;
import tv.v1x1.common.services.cache.CodecCache;
import tv.v1x1.common.services.cache.LambdaCodec;
import tv.v1x1.common.util.data.CompositeKey;

/**
 * Represents a single text chat room on a platform on which the bot is a part of
 * @author Cobi
 */
public abstract class Channel {
    public static final CodecCache.Codec<Channel> KEY_CODEC = new LambdaCodec<>(c -> CompositeKey.makeKey(ChannelGroup.KEY_CODEC.encode(c.getChannelGroup()), c.getId().getBytes(), c.getDisplayName().getBytes()), b -> {
        final byte[][] keys = CompositeKey.getKeys(b);
        final ChannelGroup channelGroup = ChannelGroup.KEY_CODEC.decode(keys[0]);
        final String id = new String(keys[1]);
        final String displayName = new String(keys[2]);
        switch(channelGroup.getPlatform()) {
            case TWITCH: return new TwitchChannel(id, channelGroup, displayName);
            case DISCORD: return new DiscordChannel(id, channelGroup, displayName);
            default: throw new IllegalStateException("Unknown channel platform " + channelGroup.getPlatform().name());
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
        switch(channel.getChannelGroup().getPlatform()) {
            case TWITCH: return TwitchChannel.fromProto(channel);
            case DISCORD: return DiscordChannel.fromProto(channel);
            default: throw new IllegalStateException("Unknown channel platform " + channel.getChannelGroup().getPlatform().name());
        }
    }

    public static Channel fromProto(final ChannelGroup channelGroup, final ChannelOuterClass.ChannelGroupEntry tenantEntry) {
        switch(channelGroup.getPlatform()) {
            case TWITCH: return TwitchChannel.fromProto(channelGroup, tenantEntry);
            case DISCORD: return DiscordChannel.fromProto(channelGroup, tenantEntry);
            default: throw new IllegalStateException("Unknown channel platform " + channelGroup.getPlatform().name());
        }
    }

    public static Channel emptyFromPlatform(final Platform platform) {
        switch(platform) {
            case TWITCH: return TwitchChannel.EMPTY;
            case DISCORD: return DiscordChannel.EMPTY;
            default: throw new IllegalStateException("Unknown channel platform " + platform.name());
        }
    }

    protected String id;
    protected ChannelGroup channelGroup;
    protected String displayName;

    public Channel(final String id, final ChannelGroup channelGroup, final String displayName) {
        this.id = id;
        this.channelGroup = channelGroup;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public Tenant getTenant() {
        return getChannelGroup().getTenant();
    }

    @Override
    public String toString() {
        return "{Channel/" + id + ":" + displayName + "}";
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        return getId().equals(((Channel)obj).getId());
    }

    public ChannelOuterClass.Channel toProto() {
        return ChannelOuterClass.Channel.newBuilder()
                .setId(id)
                .setDisplayName(displayName)
                .setChannelGroup(channelGroup.toProto())
                .build();
    }

    public ChannelOuterClass.ChannelGroupEntry toProtoChannelGroupEntry() {
        return ChannelOuterClass.ChannelGroupEntry.newBuilder()
                .setId(id)
                .setDisplayName(displayName)
                .build();
    }

    public tv.v1x1.common.dto.db.Channel toDB() {
        return new tv.v1x1.common.dto.db.Channel(channelGroup.getPlatform(), id, displayName, channelGroup.getId());
    }

    public Platform getPlatform() {
        return channelGroup.getPlatform();
    }

    public abstract String getMention();
}
