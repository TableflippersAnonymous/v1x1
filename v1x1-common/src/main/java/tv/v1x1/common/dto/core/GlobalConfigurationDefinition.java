package tv.v1x1.common.dto.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/24/2016.
 */
public class GlobalConfigurationDefinition extends ConfigurationDefinition {
    public GlobalConfigurationDefinition(final String name, final String displayName, final String description,
                                         final int version, final tv.v1x1.common.scanners.config.Permission tenantPermission,
                                         final List<Field> fields, final Map<String, List<Field>> complexFields) {
        super(name, displayName, description, version, tenantPermission, fields, complexFields);
    }

    @Override
    public tv.v1x1.common.dto.db.GlobalConfigurationDefinition toDB() {
        return new tv.v1x1.common.dto.db.GlobalConfigurationDefinition(getName(), getDisplayName(), getDescription(),
                getVersion(), getTenantPermission(), getFields().stream().map(Field::toDB).collect(Collectors.toList()),
                getComplexFields().entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(Field::toDB).collect(Collectors.toList())
                )));
    }
}
