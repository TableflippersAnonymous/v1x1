package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Table;
import tv.v1x1.common.config.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by naomi on 11/6/2016.
 */
@Table(name = "channel_configuration_definition")
public class ChannelConfigurationDefinition extends ConfigurationDefinition {
    public ChannelConfigurationDefinition() {
    }

    public ChannelConfigurationDefinition(final String name, final String displayName, final String description,
                                         final int version, final tv.v1x1.common.config.Permission tenantPermission, final List<Field> fields,
                                         final Map<String, List<Field>> complexFields) {
        super(name, displayName, description, version, tenantPermission, fields, complexFields);
    }

    @Override
    public tv.v1x1.common.dto.core.ChannelConfigurationDefinition toCore() {
        return new tv.v1x1.common.dto.core.ChannelConfigurationDefinition(getName(), getDisplayName(), getDescription(),
                getVersion(), getTenantPermission(), getFields().stream().map(Field::toCore).collect(Collectors.toList()),
                getComplexFields().entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(Field::toCore).collect(Collectors.toList())
                )));
    }
}
