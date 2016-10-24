package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Table;
import tv.v1x1.common.config.Permission;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/24/2016.
 */
@Table(name = "tenant_configuration_definition")
public class TenantConfigurationDefinition extends ConfigurationDefinition {
    public TenantConfigurationDefinition() {
    }

    public TenantConfigurationDefinition(final String name, final String displayName, final String description, final int version, final Permission tenantPermission, final List<Field> fields) {
        super(name, displayName, description, version, tenantPermission, fields);
    }

    @Override
    public tv.v1x1.common.dto.core.TenantConfigurationDefinition toCore() {
        return new tv.v1x1.common.dto.core.TenantConfigurationDefinition(getName(), getDisplayName(), getDescription(), getVersion(), getTenantPermission(), getFields().stream().map(Field::toCore).collect(Collectors.toList()));
    }


}
