package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import tv.v1x1.common.dto.db.GlobalConfigurationDefinition;
import tv.v1x1.common.dto.db.TenantConfigurationDefinition;

/**
 * Created by cobi on 10/24/2016.
 */
public class DAOConfigurationDefinition {
    private final Mapper<GlobalConfigurationDefinition> globalMapper;
    private final Mapper<TenantConfigurationDefinition> tenantMapper;
    private final ConfigurationDefinitionAccessor accessor;

    @Accessor
    public interface ConfigurationDefinitionAccessor {
        @Query("SELECT * FROM global_configuration_definition")
        Result<GlobalConfigurationDefinition> allGlobal();

        @Query("SELECT * FROM tenant_configuration_definition")
        Result<TenantConfigurationDefinition> allTenant();
    }

    public DAOConfigurationDefinition(final MappingManager mappingManager) {
        globalMapper = mappingManager.mapper(GlobalConfigurationDefinition.class);
        tenantMapper = mappingManager.mapper(TenantConfigurationDefinition.class);
        accessor = mappingManager.createAccessor(ConfigurationDefinitionAccessor.class);
    }

    public Iterable<GlobalConfigurationDefinition> getAllGlobal() {
        return accessor.allGlobal();
    }

    public Iterable<TenantConfigurationDefinition> getAllTenant() {
        return accessor.allTenant();
    }

    public GlobalConfigurationDefinition getGlobal(final String module) {
        return globalMapper.get(module);
    }

    public TenantConfigurationDefinition getTenant(final String module) {
        return tenantMapper.get(module);
    }

    public void put(final GlobalConfigurationDefinition globalConfigurationDefinition) {
        final GlobalConfigurationDefinition oldGlobalConfigurationDefinition = getGlobal(globalConfigurationDefinition.getName());
        if(oldGlobalConfigurationDefinition == null || oldGlobalConfigurationDefinition.getVersion() <= globalConfigurationDefinition.getVersion())
            globalMapper.save(globalConfigurationDefinition);
    }

    public void put(final TenantConfigurationDefinition tenantConfigurationDefinition) {
        final TenantConfigurationDefinition oldTenantConfigurationDefinition = getTenant(tenantConfigurationDefinition.getName());
        if(oldTenantConfigurationDefinition == null || oldTenantConfigurationDefinition.getVersion() <= tenantConfigurationDefinition.getVersion())
            tenantMapper.save(tenantConfigurationDefinition);
    }
}
