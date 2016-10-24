package tv.v1x1.common.dto.core;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/24/2016.
 */
public class TenantConfigurationDefinition extends ConfigurationDefinition {

    public TenantConfigurationDefinition(final String name, final String displayName, final String description, final int version, final tv.v1x1.common.config.Permission tenantPermission, final List<Field> fields) {
        super(name, displayName, description, version, tenantPermission, fields);
    }

    @Override
    public tv.v1x1.common.dto.db.TenantConfigurationDefinition toDB() {
        return new tv.v1x1.common.dto.db.TenantConfigurationDefinition(getName(), getDisplayName(), getDescription(), getVersion(), getTenantPermission(), getFields().stream().map(Field::toDB).collect(Collectors.toList()));
    }
}
