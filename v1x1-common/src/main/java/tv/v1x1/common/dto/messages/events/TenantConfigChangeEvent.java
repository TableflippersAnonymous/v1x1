package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Created by naomi on 2/17/2017.
 */
public class TenantConfigChangeEvent extends ConfigChangeEvent {
    private final Tenant tenant;

    public TenantConfigChangeEvent(final Module from, final Module configModule, final Tenant tenant) {
        super(from, configModule);
        this.tenant = tenant;
    }

    public TenantConfigChangeEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final Module configModule, final Tenant tenant) {
        super(from, messageId, timestamp, context, configModule);
        this.tenant = tenant;
    }

    public Tenant getTenant() {
        return tenant;
    }

    @Override
    protected EventOuterClass.ConfigChangeEvent.Builder toProtoConfigChange() {
        return super.toProtoConfigChange()
                .setConfigType(EventOuterClass.ConfigChangeEvent.ConfigType.TENANT)
                .setTenant(tenant.toProto());
    }
}
