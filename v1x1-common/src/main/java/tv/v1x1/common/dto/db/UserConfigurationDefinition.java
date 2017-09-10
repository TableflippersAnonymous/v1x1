package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Table;
import tv.v1x1.common.config.Permission;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/24/2016.
 */
@Table(name = "user_configuration_definition")
public class UserConfigurationDefinition extends ConfigurationDefinition {
    public UserConfigurationDefinition() {
    }

    public UserConfigurationDefinition(final String name, final String displayName, final String description,
                                       final int version, final Permission tenantPermission, final List<Field> fields,
                                       final Map<String, List<Field>> complexFields) {
        super(name, displayName, description, version, tenantPermission, fields, complexFields);
    }

    @Override
    public tv.v1x1.common.dto.core.UserConfigurationDefinition toCore() {
        return new tv.v1x1.common.dto.core.UserConfigurationDefinition(getName(), getDisplayName(), getDescription(),
                getVersion(), getTenantPermission(), getFields().stream().map(Field::toCore).collect(Collectors.toList()),
                getComplexFields().entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(Field::toCore).collect(Collectors.toList())
                )));
    }
}
