package tv.v1x1.common.dto.core;

import tv.v1x1.common.dto.proto.core.ChannelOuterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a group of {@link Channel}s the bot will treat the same
 * @author Naomi
 */
public class Tenant {
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
}
