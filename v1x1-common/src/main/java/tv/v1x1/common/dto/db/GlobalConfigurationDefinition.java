package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Table;
import tv.v1x1.common.scanners.config.Permission;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/24/2016.
 */
@Table(name = "global_configuration_definition")
public class GlobalConfigurationDefinition extends ConfigurationDefinition {
    public GlobalConfigurationDefinition() {
    }

    public GlobalConfigurationDefinition(final String name, final String displayName, final String description,
                                         final int version, final Permission tenantPermission, final List<Field> fields,
                                         final Map<String, List<Field>> complexFields) {
        super(name, displayName, description, version, tenantPermission, fields, complexFields);
    }

    @Override
    public tv.v1x1.common.dto.core.GlobalConfigurationDefinition toCore() {
        return new tv.v1x1.common.dto.core.GlobalConfigurationDefinition(getName(), getDisplayName(), getDescription(),
                getVersion(), getTenantPermission(), getFields().stream().map(Field::toCore).collect(Collectors.toList()),
                getComplexFields().entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(Field::toCore).collect(Collectors.toList())
                )));
    }
}
