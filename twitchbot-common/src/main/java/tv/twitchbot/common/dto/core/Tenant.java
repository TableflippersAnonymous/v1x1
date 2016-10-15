package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.TenantOuterClass;

/**
 * Created by naomi on 10/6/2016.
 */
public class Tenant {
    public static Tenant fromProto(TenantOuterClass.Tenant tenant) {
        UUID uuid = UUID.fromProto(tenant.getId());
        return new Tenant(uuid);
    }

    private UUID id;

    public Tenant(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public TenantOuterClass.Tenant toProto() {
        return TenantOuterClass.Tenant.newBuilder()
                .setId(id.toProto())
                .build();
    }

    public tv.twitchbot.common.dto.db.Tenant toDB() {
        return new tv.twitchbot.common.dto.db.Tenant(id.getValue());
    }
}
