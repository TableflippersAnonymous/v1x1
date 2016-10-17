package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ChannelOuterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a group of {@link Channel}s the bot will treat the same
 * @author Naomi
 */
public class Tenant {
    public static Tenant fromProto(ChannelOuterClass.Tenant proto) {
        UUID uuid = UUID.fromProto(proto.getId());
        List<Channel> channels = new ArrayList<>();
        Tenant tenant = new Tenant(uuid, channels);
        channels.addAll(proto.getEntriesList().stream().map(entry -> Channel.fromProto(tenant, entry)).collect(Collectors.toList()));
        return tenant;
    }

    private UUID id;
    private List<Channel> channels;

    public Tenant(UUID id, List<Channel> channels) {
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

    public tv.twitchbot.common.dto.db.Tenant toDB() {
        return new tv.twitchbot.common.dto.db.Tenant(id.getValue(), channels.stream().map(Channel::toDBTenant).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tenant tenant = (Tenant) o;

        return id != null ? id.equals(tenant.id) : tenant.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
